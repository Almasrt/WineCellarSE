package persistence.dao.user;

import com.mongodb.MongoWriteException;
import com.mongodb.client.model.Updates;
import constant.CollectionNames;
import exception.InvalidUsernameException;
import exception.NotFoundException;
import org.bson.BsonDocument;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import persistence.dao.AbstractDao;
import persistence.entity.user.User;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Updates.combine;

public class UserDao extends AbstractDao<User> {

	private static UserDao instance;

	private UserDao() { }

	public static UserDao getInstance() {
		if (instance == null) {
			instance = new UserDao();
		}
		return instance;
	}

	@Override
	protected String getCollectionName() {
		return CollectionNames.USER;
	}

	@Override
	protected Class<User> getEntityClass() {
		return User.class;
	}

	@Override
	protected Bson getSetOnUpdate(User entity) {
		List<Bson> updateList = new ArrayList<>();

		updateList.add(Updates.set("username", entity.getUsername()));
		updateList.add(Updates.set("password", entity.getPassword()));

		return combine(updateList);
	}

	/**
	 * Insert one entity of the parametrized type in the database.
	 *
	 * @param user The entity to insert.
	 * @return The id of the inserted entity.
	 */
	@Override
	public ObjectId insertOne(User user) throws InvalidUsernameException {
		try	{
			return super.insertOne(user);
		}
		catch (MongoWriteException e) {
			if (e.getCode() == 11000) {
				throw new InvalidUsernameException();
			}
			throw e;
		}
	}

	/**
	 * Find a user by its username.
	 *
	 * @param username The username of the user to find.
	 * @return The user if found. Otherwise, throw a NotFoundException.
	 * @throws NotFoundException if the user is not found.
	 */
	public User findOneByUsername(String username) throws NotFoundException {
		BsonDocument filter = new BsonDocument();
		filter.append("username", new org.bson.BsonString(username));
		User user = super.findOne(filter);

		if (user == null) {
			throw new NotFoundException();
		}
		else {
			return user;
		}
	}
}
