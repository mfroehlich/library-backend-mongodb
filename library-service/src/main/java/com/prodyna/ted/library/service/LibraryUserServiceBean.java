package com.prodyna.ted.library.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.xml.registry.infomodel.User;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.prodyna.ted.library.entity.Book;
import com.prodyna.ted.library.entity.Category;
import com.prodyna.ted.library.entity.LibraryUser;

/**
 * @author Iryna Feuerstein (PRODYNA AG)
 *
 */
@Stateless
public class LibraryUserServiceBean implements LibraryUserService {
	
	private static final String LENT_BOOKS = "lentBooks";
    private static final String TELEPHONE_NUMBER = "telephoneNumber";
    private static final String DATE_OF_BIRTH = "dateOfBirth";
    private static final String LAST_NAME = "lastName";
    private static final String FIRST_NAME = "firstName";
    private static final String USERNAME = "username";
    private static final String LIBRARY_USER_ID = "libraryUserID";
    private static final String LIBRARY_USER = "LibraryUsers";
    
	@Inject
	private MongoDatabase libraryDB;

	@Override
	public void addUser(LibraryUser user) {
		Document userDoc = userToDocument(user);
		libraryDB.getCollection(LIBRARY_USER).insertOne(userDoc);
	}

	@Override
	public void removeUser(final UUID uuid) {
		libraryDB.getCollection(LIBRARY_USER).findOneAndDelete(Filters.eq(LIBRARY_USER_ID, uuid)) ;
	}

	@Override
	public LibraryUser findUserByUUID(UUID uuid) {
	    Document userDoc = libraryDB.getCollection(LIBRARY_USER).find(Filters.eq(LIBRARY_USER_ID, uuid)).iterator().next() ;
	    LibraryUser user = documentToUser(userDoc);
	    return user;
	}

    @Override
	public List<LibraryUser> findUsersByUsername(String username) {
        FindIterable<Document> userDoc = libraryDB.getCollection(LIBRARY_USER).find(Filters.eq(USERNAME, username)) ;
        List<LibraryUser> users = documentToUser(userDoc);
        return users;
	}


    @Override
	public List<LibraryUser> findUsersByFirstName(String firstname) {
        FindIterable<Document> userDoc = libraryDB.getCollection(LIBRARY_USER).find(Filters.eq(FIRST_NAME   , firstname)) ;
        List<LibraryUser> users = documentToUser(userDoc);
        return users;
	}

	@Override
	public List<LibraryUser> findUsersByLastName(String lastname) {
        FindIterable<Document> userDoc = libraryDB.getCollection(LIBRARY_USER).find(Filters.eq(LAST_NAME   , lastname)) ;
        List<LibraryUser> users = documentToUser(userDoc);
        return users;
	}

	@Override
	public List<LibraryUser> findUsersByDateOfBirth(Date dateOfBirth) {
        FindIterable<Document> userDoc = libraryDB.getCollection(LIBRARY_USER).find(Filters.eq(DATE_OF_BIRTH   , dateOfBirth)) ;
        List<LibraryUser> users = documentToUser(userDoc);
        return users;
	}

	@Override
	public LibraryUser findUserByBookLent(Book book) {
		// TODO your code comes here
		return null;
	}

	@Override
	public List<LibraryUser> findUsersByBooksLentInCategory(Category category) {
		// TODO your code comes here
		return null;
	}

	@Override
	public void lendBook(Book book, LibraryUser user) {
		// TODO your code comes here
		
	}

	@Override
	public List<Book> findBooksLentByUser(LibraryUser user) {
		// TODO your code comes here
		return null;
	}
	
	@Override
	public void removeAll() {
        libraryDB.getCollection(LIBRARY_USER).drop();
	}

	@Override
	public List<LibraryUser> findAll() {
        FindIterable<Document> userDocs = libraryDB.getCollection(LIBRARY_USER).find();

        List<LibraryUser> users = new ArrayList<>();
        for (Document userDoc : userDocs) {
            LibraryUser user = documentToUser(userDoc);
            users.add(user);
        }
        return users;
	}

    private Document userToDocument(LibraryUser user) {
        Document userDoc =new Document();
                
        userDoc.put(LIBRARY_USER_ID, user.getLibraryUserID());
        userDoc.put(USERNAME, user.getUsername());
        userDoc.put(FIRST_NAME, user.getFirstName());
        userDoc.put(LAST_NAME, user.getLastName());
        userDoc.put(DATE_OF_BIRTH, user.getDateOfBirth());
        userDoc.put(TELEPHONE_NUMBER, user.getTelephoneNumber());
        userDoc.put(LENT_BOOKS, user.getLentBooks());
        return userDoc;
    }
    private LibraryUser documentToUser(Document userDoc) {
        UUID uuid = (UUID) userDoc.get(LIBRARY_USER_ID);
        LibraryUser user = new LibraryUser(userDoc.getString(FIRST_NAME), userDoc.getString(LAST_NAME));
        user.setUsername(userDoc.getString(USERNAME));
        user.setLibraryUserID(uuid);
        user.setDateOfBirth(userDoc.getDate(DATE_OF_BIRTH));
        user.setTelephoneNumber(userDoc.getString(TELEPHONE_NUMBER));
        user.getLentBooks().addAll((Collection<Book>) userDoc.get(LENT_BOOKS));
        return user;
    }
    private List<LibraryUser> documentToUser(FindIterable<Document> userDoc) {
        List<LibraryUser> users = new ArrayList<LibraryUser>();
        for (Document document : userDoc) {
            users.add(documentToUser(document));
        }
        return users;
    }
    
}
