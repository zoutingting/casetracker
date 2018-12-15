package framework;

import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Transaction;

public class DatastoreHelper {

	public enum DBEntity {
		User, CaseTracker
	}

	public enum User {
		googleEmail, permissions
	}

	public enum CaseTracker {
		timestamp, creator, members, messages, status, subject
	}

	public enum CaseTracker_Status {
		created(10), closed(90);

		private int numVal;

		CaseTracker_Status(int numVal) {
			this.numVal = numVal;
		}

		public String getNumVal() {
			return String.valueOf(numVal);
		}
	}

	private static final String KEY = "key";
	private static final String ID = "id";

	private static final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public Map<String, Map<String, Object>> getUsers() {
		Map<String, Map<String, Object>> beans = new HashMap<String, Map<String, Object>>();
		Query q = new Query(DBEntity.User.toString()).addSort(User.googleEmail.toString(), SortDirection.ASCENDING);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity entity : pq.asIterable()) {
			Map<String, Object> properties = new HashMap<String, Object>(entity.getProperties());
			properties.put(KEY, KeyFactory.keyToString(entity.getKey()));
			properties.put(ID, String.valueOf(entity.getKey().getId()));
			beans.put((String) entity.getProperty(User.googleEmail.toString()), properties);
		}
		return beans;
	}

	public Map<String, Map<String, Object>> getCaseTrackers() {
		Map<String, Map<String, Object>> beans = new HashMap<String, Map<String, Object>>();
		Query q = new Query(DBEntity.CaseTracker.toString()).addSort(CaseTracker.timestamp.toString(), SortDirection.ASCENDING);
		PreparedQuery pq = datastore.prepare(q);
		for (Entity entity : pq.asIterable()) {
			Map<String, Object> properties = new HashMap<String, Object>(entity.getProperties());
			properties.put(KEY, KeyFactory.keyToString(entity.getKey()));
			properties.put(ID, String.valueOf(entity.getKey().getId()));
			beans.put(properties.get(ID).toString(), properties);
		}
		return beans;
	}

	public Map<String, Object> getEntityProperties(String serializedKey) {
		Map<String, Object> properties = null;
		try {
			Key deserializedKey = KeyFactory.stringToKey(serializedKey);
			Entity entity = datastore.get(deserializedKey);
			properties = new HashMap<String, Object>(entity.getProperties());
			properties.put(KEY, KeyFactory.keyToString(entity.getKey()));
			properties.put(ID, String.valueOf(entity.getKey().getId()));
		} catch (EntityNotFoundException e) {
			e.printStackTrace();
		}
		return properties;
	}

	public void reviseEntity(String serializedKey, Map<String, Object> properties) {
		Transaction txn = datastore.beginTransaction();
		try {
			Key deserializedKey = KeyFactory.stringToKey(serializedKey);
			Entity entity = datastore.get(deserializedKey);
			for (Map.Entry<String, Object> e : properties.entrySet())
				entity.setProperty(e.getKey(), e.getValue());
			datastore.put(entity);
			txn.commit();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	}

	public void removeEntity(String serializedKey) {
		Transaction txn = datastore.beginTransaction();
		try {
			Key deserializedKey = KeyFactory.stringToKey(serializedKey);
			datastore.delete(deserializedKey);
			txn.commit();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	}

	public void addCaseTracker(String subject, String timestamp, String creator, String status, String members, String message) {
		Transaction txn = datastore.beginTransaction();
		try {
			Entity caseTracker = new Entity(DBEntity.CaseTracker.toString());
			caseTracker.setProperty(CaseTracker.subject.toString(), subject);
			caseTracker.setProperty(CaseTracker.timestamp.toString(), timestamp);
			caseTracker.setProperty(CaseTracker.creator.toString(), creator);
			caseTracker.setProperty(CaseTracker.status.toString(), status);
			caseTracker.setProperty(CaseTracker.members.toString(), members);
			caseTracker.setProperty(CaseTracker.messages.toString(), new Text(message));
			datastore.put(caseTracker);
			txn.commit();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (txn.isActive()) {
				txn.rollback();
			}
		}
	}
}