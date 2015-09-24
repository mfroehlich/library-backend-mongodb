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
	

    
    @Inject
    private EntityDocumentTransformer transformer;
    
	@Inject
	private MongoDatabase libraryDB;

	@Override
	public void addUser(LibraryUser user) {
		Document userDoc = transformer.userToDocument(user);
		libraryDB.getCollection(EntityDocumentTransformer.LIBRARY_USER).insertOne(userDoc);
	}

	@Override
	public void removeUser(final UUID uuid) {
		libraryDB.getCollection(EntityDocumentTransformer.LIBRARY_USER).findOneAndDelete(Filters.eq(EntityDocumentTransformer.LIBRARY_USER_ID, uuid)) ;
	}

	@Override
	public LibraryUser findUserByUUID(UUID uuid) {
	    Document userDoc = libraryDB.getCollection(EntityDocumentTransformer.LIBRARY_USER).find(Filters.eq(EntityDocumentTransformer.LIBRARY_USER_ID, uuid)).iterator().next() ;
	    LibraryUser user = transformer.documentToUser(userDoc);
	    return user;
	}

    @Override
	public List<LibraryUser> findUsersByUsername(String username) {
        FindIterable<Document> userDoc = libraryDB.getCollection(EntityDocumentTransformer.LIBRARY_USER).find(Filters.eq(EntityDocumentTransformer.USERNAME, username)) ;
        List<LibraryUser> users = transformer.documentToUser(userDoc);
        return users;
	}


    @Override
	public List<LibraryUser> findUsersByFirstName(String firstname) {
        FindIterable<Document> userDoc = libraryDB.getCollection(EntityDocumentTransformer.LIBRARY_USER).find(Filters.eq(EntityDocumentTransformer.FIRST_NAME   , firstname)) ;
        List<LibraryUser> users = transformer.documentToUser(userDoc);
        return users;
	}

	@Override
	public List<LibraryUser> findUsersByLastName(String lastname) {
        FindIterable<Document> userDoc = libraryDB.getCollection(EntityDocumentTransformer.LIBRARY_USER).find(Filters.eq(EntityDocumentTransformer.LAST_NAME   , lastname)) ;
        List<LibraryUser> users = transformer.documentToUser(userDoc);
        return users;
	}

	@Override
	public List<LibraryUser> findUsersByDateOfBirth(Date dateOfBirth) {
        FindIterable<Document> userDoc = libraryDB.getCollection(EntityDocumentTransformer.LIBRARY_USER).find(Filters.eq(EntityDocumentTransformer.DATE_OF_BIRTH   , dateOfBirth)) ;
        List<LibraryUser> users = transformer.documentToUser(userDoc);
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

    
}
