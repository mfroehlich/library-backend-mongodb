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
	
	@Override
	public void addBook(Book book) {
        Document bookDoc = getDocumentForBook(book);
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
            book = getBookForDocument(bookDoc);
        }

		return book;
	}

    @Override
	public List<Book> findBooksByTitle(String title) {

        List<Book> books = new ArrayList<>();
        FindIterable<Document> bookDocs = libraryDB.getCollection(BOOKS).find(eq("title", title));
        for (Document bookDoc : bookDocs) {
            Book book = getBookForDocument(bookDoc);
            books.add(book);
        }

        return books;
	}

	@Override
	public List<Book> findBooksByAuthor(String author) {

        List<Book> books = new ArrayList<>();
        FindIterable<Document> booksDocs = libraryDB.getCollection(BOOKS).find(eq("authors", author));
        for (Document bookDoc : booksDocs) {
            Book book = getBookForDocument(bookDoc);
            books.add(book);
        }
		
		return books;
	}

	@Override
	public List<Book> findBooksByCategory(Category category) {

        List<Book> books = new ArrayList<>();
        FindIterable<Document> booksDocs = libraryDB.getCollection(BOOKS).find(eq("categories.name", category.getName()));
        for (Document bookDoc : booksDocs) {
            Book book = getBookForDocument(bookDoc);
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
            Book book = getBookForDocument(bookDoc);
            books.add(book);
        }

        return books;
	}

    private Document getDocumentForBook(Book book) {
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

    private Book getBookForDocument(Document bookDoc) {
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

    private Document getDocumentForCategory(Category cat) {
        Document doc = new Document();
        doc.put("name", cat.getName());
        return doc;
    }

    private Category getCategoryForDocument(Document catDoc) {
        String name = (String) catDoc.get("name");
        Category c = new Category(name);
        return c;
    }
}
