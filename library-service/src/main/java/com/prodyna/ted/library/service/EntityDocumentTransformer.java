package com.prodyna.ted.library.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.prodyna.ted.library.entity.Book;
import com.prodyna.ted.library.entity.Category;
import com.prodyna.ted.library.entity.LibraryUser;

public class EntityDocumentTransformer {

    public static final String LENT_BOOKS = "lentBooks";
    public static final String TELEPHONE_NUMBER = "telephoneNumber";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String LAST_NAME = "lastName";
    public static final String FIRST_NAME = "firstName";
    public static final String USERNAME = "username";
    public static final String LIBRARY_USER_ID = "libraryUserID";
    public static final String LIBRARY_USER = "LibraryUsers";
    
    public Document getDocumentForBook(Book book) {
        Document doc = new Document();
        doc.put("isbn", book.getIsbn());
        doc.put("title", book.getTitle());
        doc.put("subtitle", book.getSubtitle());
        doc.put("authors", book.getAuthors());

        List<Document> catDocs = new ArrayList<>();
        for (Category cat: book.getCategories()) {
            catDocs.add(getDocumentForCategory(cat));
        }
        doc.put("categories", catDocs);

        return doc;
    }
    

    public Document getDocumentForCategory(Category cat) {
        Document doc = new Document();
        doc.put("name", cat.getName());
        return doc;
    }
    
    public Book getBookForDocument(Document bookDoc) {
        String title = (String) bookDoc.get("title");
        String isbn = (String) bookDoc.get("isbn");
        String subtitle = (String) bookDoc.get("subtitle");
        List<Document> catDocs = (List<Document>) bookDoc.get("categories");
        List<String> authors = (List<String>) bookDoc.get("authors");

        Book b = new Book(title, isbn);
        b.setSubtitle(subtitle);

        for (String author : authors) {
            b.addAuthor(author);
        }
        for (Document catDoc : catDocs) {
            Category c = getCategoryForDocument(catDoc);
            b.addCategory(c);
        }

        return b;
    }

    public Category getCategoryForDocument(Document catDoc) {
        String name = (String) catDoc.get("name");
        Category c = new Category(name);
        return c;
    }
    

    public Document userToDocument(LibraryUser user) {
        Document userDoc =new Document();
        userDoc.put(LIBRARY_USER_ID, user.getLibraryUserID());
        userDoc.put(USERNAME, user.getUsername());
        userDoc.put(FIRST_NAME, user.getFirstName());
        userDoc.put(LAST_NAME, user.getLastName());
        userDoc.put(DATE_OF_BIRTH, user.getDateOfBirth());
        userDoc.put(TELEPHONE_NUMBER, user.getTelephoneNumber());
        
        List<Book> lentBooks = user.getLentBooks();
        List<Document> booksDoc = new ArrayList<Document>();
        for (Book book : lentBooks) {
            booksDoc.add(getDocumentForBook(book));
        }
        userDoc.put(LENT_BOOKS, lentBooks);
        
        
        return userDoc;
    }
    public LibraryUser documentToUser(Document userDoc) {
        UUID uuid = (UUID) userDoc.get(LIBRARY_USER_ID);
        LibraryUser user = new LibraryUser(userDoc.getString(FIRST_NAME), userDoc.getString(LAST_NAME));
        user.setUsername(userDoc.getString(USERNAME));
        user.setLibraryUserID(uuid);
        user.setDateOfBirth(userDoc.getDate(DATE_OF_BIRTH));
        user.setTelephoneNumber(userDoc.getString(TELEPHONE_NUMBER));
        Collection<Document> object = (Collection<Document>) userDoc.get(LENT_BOOKS);
        for (Document document : object) {
            user.getLentBooks().add(getBookForDocument(document)); 
        }
        return user;
    }
    public List<LibraryUser> documentToUser(FindIterable<Document> userDoc) {
        List<LibraryUser> users = new ArrayList<LibraryUser>();
        for (Document document : userDoc) {
            users.add(documentToUser(document));
        }
        return users;
    }
}
