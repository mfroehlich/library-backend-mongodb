package com.prodyna.ted.library.service;

import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.mongodb.client.model.Filters;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.prodyna.ted.library.entity.Book;
import com.prodyna.ted.library.entity.Category;
import org.bson.conversions.Bson;

/**
 * @author Iryna Feuerstein (PRODYNA AG)
 *
 */
@Stateless
public class BookServiceBean implements BookService {

private static final String BOOKS = "books";

	@Inject
	private MongoDatabase libraryDB;

	@Inject
	private EntityDocumentTransformer transformer;
	
	@Override
	public void addBook(Book book) {
        Document bookDoc = transformer.getDocumentForBook(book);
        libraryDB.getCollection(BOOKS).insertOne(bookDoc);
	}

    @Override
	public void removeBook(String isbn) {
        Bson isbnEq = eq("isbn", isbn);
        libraryDB.getCollection(BOOKS).deleteOne(isbnEq);
	}

	@Override
	public Book findBookByISBN(String isbn) {

        Book book = null;
        FindIterable<Document> books = libraryDB.getCollection(BOOKS).find(eq("isbn", isbn));
        for (Document bookDoc : books) {
            book = transformer.getBookForDocument(bookDoc);
        }

		return book;
	}

    @Override
	public List<Book> findBooksByTitle(String title) {

        List<Book> books = new ArrayList<>();
        FindIterable<Document> bookDocs = libraryDB.getCollection(BOOKS).find(eq("title", title));
        for (Document bookDoc : bookDocs) {
            Book book = transformer.getBookForDocument(bookDoc);
            books.add(book);
        }

        return books;
	}

	@Override
	public List<Book> findBooksByAuthor(String author) {

        List<Book> books = new ArrayList<>();
        FindIterable<Document> booksDocs = libraryDB.getCollection(BOOKS).find(eq("authors", author));
        for (Document bookDoc : booksDocs) {
            Book book = transformer.getBookForDocument(bookDoc);
            books.add(book);
        }
		
		return books;
	}

	@Override
	public List<Book> findBooksByCategory(Category category) {

        List<Book> books = new ArrayList<>();
        FindIterable<Document> booksDocs = libraryDB.getCollection(BOOKS).find(eq("categories.name", category.getName()));
        for (Document bookDoc : booksDocs) {
            Book book =transformer. getBookForDocument(bookDoc);
            books.add(book);
        }

        return books;
	}

	@Override
	public void removeAll() {
        libraryDB.getCollection(BOOKS).drop();
	}

	@Override
	public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        FindIterable<Document> allBooks = libraryDB.getCollection(BOOKS).find();
        for (Document bookDoc : allBooks) {
            Book book = transformer.getBookForDocument(bookDoc);
            books.add(book);
        }

        return books;
	}
}
