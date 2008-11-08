/*
 * This is a common dao with basic CRUD operations and is not limited to any 
 * persistent layer implementation
 * 
 * Copyright (C) 2008  Imran M Yousuf
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.smartitengineering.dao.impl.hibernate;

import com.smartitengineering.dao.common.QueryParameter;
import com.smartitengineering.dao.impl.hibernate.domain.Author;
import com.smartitengineering.dao.impl.hibernate.domain.Book;
import com.smartitengineering.dao.impl.hibernate.domain.Publisher;
import com.smartitengineering.domain.PersistentDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author imyousuf
 */
public class AbstractDAOTest
    extends TestCase {

    private static ApplicationContext context;

    public AbstractDAOTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp()
        throws Exception {
        super.setUp();
        if (context == null) {
            context = new ClassPathXmlApplicationContext("app-context.xml");
        }
    }

    @Override
    protected void tearDown()
        throws Exception {
        super.tearDown();
    }

    /**
     * Test of createEntity method, of class AbstractDAO.
     */
    public void testCreateEntity() {
        System.out.println("createEntity");
        AbstractDAO<Book> bookInstance = getDaoInstance();
        AbstractDAO<Author> authorInstance = getDaoInstance();
        AbstractDAO<Publisher> publisherInstance = getDaoInstance();
        /**
         * Test null object creation, should throw IllegalArgumentException
         */
        try {
            bookInstance.createEntity((Book) null);
            fail("Should not proceed with saving NULL entity!");
        }
        catch (IllegalArgumentException argumentException) {
        }
        catch (Exception exception) {
            fail(exception.getMessage());
        }
        /**
         * Create proper test data.
         * This set will contain single of everything - author, publisher, book
         */
        Publisher shebaProkashani = getShebaProkashani();
        try {
            publisherInstance.createEntity(shebaProkashani);
            System.out.println(shebaProkashani.getId());
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }
        Author kaziAnowarHossain = getKaziAnowarHossain();
        try {
            authorInstance.createEntity(kaziAnowarHossain);
            System.out.println(kaziAnowarHossain.getId());
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }
        Book book = getAgniSopoth(kaziAnowarHossain, shebaProkashani);
        try {
            bookInstance.createEntity(book);
            System.out.println(book.getId());
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }
        /**
         * Create multiple publications at once.
         * One of the publications should have more than one book
         */
        Publisher oReilly = getOReilly();
        Publisher annoProkash = getAnnoProkash();
        try {
            publisherInstance.createEntity(oReilly, annoProkash);
            System.out.println(oReilly.getId() + ", " + annoProkash.getId());
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }
        /**
         * Create multiple authors at once.
         * There should be at least one book with multiple authors.
         * A single author (set) should have multiple books.
         */
        Author brettMcLaugblin = getBrett();
        Author davidLane = getDavidLane();
        Author hughWilliams = getHughWilliams();
        Author humayunAhmed = getHumayunAhmed();
        try {
            authorInstance.createEntity(brettMcLaugblin, davidLane, humayunAhmed,
                hughWilliams);
            System.out.println(brettMcLaugblin.getId() + ", " +
                davidLane.getId() + ", " + humayunAhmed.getId() + ", " +
                hughWilliams.getId());
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }
        Book kothaoKeoNei = getKothaoKeoNei(annoProkash, humayunAhmed);
        Book agunerPoroshMoni = getAgunerPoroshMoni(annoProkash, humayunAhmed);
        Book webDbApp = getWebDbApp(oReilly, davidLane, hughWilliams);
        Book javaAndXml = getJavaAndXml(oReilly, brettMcLaugblin);
        try {
            bookInstance.createEntity(kothaoKeoNei, agunerPoroshMoni, webDbApp,
                javaAndXml);
            System.out.println(kothaoKeoNei.getId() + ", " +
                agunerPoroshMoni.getId() + ", " + webDbApp.getId() + ", " +
                javaAndXml.getId());
        }
        catch (Exception ex) {
            fail(ex.getMessage());
        }
    }

    /**
     * Test of readList method, of class AbstractDAO.
     */
    public void testReadList_Class_QueryParameterArr() {
        System.out.println("readList");
        /**
         * Add test for read all
         */
        AbstractDAO<Book> bookInstance = getDaoInstance();
        Set<Book> books = getAll(bookInstance, Book.class);
        assertEquals(5, books.size());
        AbstractDAO<Author> authorInstance = getDaoInstance();
        Set<Author> authors = getAll(authorInstance, Author.class);
        assertEquals(5, authors.size());
        AbstractDAO<Publisher> publisherInstance = getDaoInstance();
        Set<Publisher> publishers = getAll(publisherInstance, Publisher.class);
        assertEquals(3, publishers.size());
    }

    /**
     * Test of readSingle method, of class AbstractDAO.
     */
    public void testReadSingle_Class_QueryParameterArr() {
        System.out.println("readSingleArgs");
        AbstractDAO<Book> bookInstance = getDaoInstance();
        AbstractDAO<Author> authorInstance = getDaoInstance();
        AbstractDAO<Publisher> publisherInstance = getDaoInstance();
        Map<String, Integer> bookNameToIdMap = new HashMap<String, Integer>();
        Map<String, Integer> publisherNameToIdMap =
            new HashMap<String, Integer>();
        Map<String, Integer> authorNameToIdMap = new HashMap<String, Integer>();
        makeNameToIdMap(bookNameToIdMap, bookInstance, authorNameToIdMap,
            authorInstance, publisherNameToIdMap, publisherInstance);
        /**
         * Test non single request
         */
        try {
            bookInstance.readSingle(Book.class);
            fail("Should not succeed retrieving 1 Book!");
        }
        catch (IllegalArgumentException argumentException) {
        }
        catch (Exception exception) {
            fail(exception.getMessage());
        }
        QueryParameter<Integer> param =
            getIdQueryParam();
        /**
         * Try to load a book with non-existing id
         */
        Book nonExistingBook = bookInstance.readSingle(Book.class, param);
        assertNull(nonExistingBook);
        /**
         * Test a random single book with id
         */
        {
            Book kothaoKeoNei = getKothaoKeoNei(null, null);
            int bookId = bookNameToIdMap.get(kothaoKeoNei.getName());
            param.setParameter(bookId);
            Book kothaoKeoNeiFromDao =
                bookInstance.readSingle(Book.class, param);
            assertBook(kothaoKeoNei, kothaoKeoNeiFromDao, bookNameToIdMap, 1);
        }
        /**
         * Test a random single publisher with id
         */
        {
            Publisher annoProkash = getAnnoProkash();
            int publisherId = publisherNameToIdMap.get(annoProkash.getName());
            param.setParameter(publisherId);
            Publisher annoProkashFromDao = publisherInstance.readSingle(
                Publisher.class,
                param);
            assertPublisher(annoProkash, annoProkashFromDao,
                publisherNameToIdMap);
        }
        /**
         * Test a random single author with id
         */
        {
            Author humayunAhmed = getHumayunAhmed();
            int authorId = authorNameToIdMap.get(humayunAhmed.getName());
            param.setParameter(authorId);
            Author humayunAhmedFromDao = authorInstance.readSingle(Author.class,
                param);
            assertAuthor(humayunAhmed, humayunAhmedFromDao, authorNameToIdMap);
        }
        QueryParameter<String> strParam =
            getNameQueryParam();
        /**
         * Test different match modes
         */
        /**
         * Test a random single book with exact name
         */
        {
            Book webDbApp = getWebDbApp(null, null, null);
            String bookName = webDbApp.getName();
            strParam.setParameter(bookName);
            strParam.setMatchMode(QueryParameter.MatchMode.EXACT);
            Book webDbAppFromDao =
                bookInstance.readSingle(Book.class, strParam);
            final int numOfAuthors = 2;
            assertBook(webDbApp, webDbAppFromDao, bookNameToIdMap, numOfAuthors);
        }
        /**
         * Test a random single author with start and end match of name
         */
        {
            Author brett = getBrett();
            strParam.setParameter(brett.getName().substring(0, 4));
            strParam.setMatchMode(QueryParameter.MatchMode.START);
            Author brettFromDao = authorInstance.readSingle(Author.class,
                strParam);
            assertAuthor(brett, brettFromDao, authorNameToIdMap);
            Author anwar = getKaziAnowarHossain();
            strParam.setParameter(anwar.getName().substring(4));
            strParam.setMatchMode(QueryParameter.MatchMode.END);
            Author anwarFromDao = authorInstance.readSingle(Author.class,
                strParam);
            assertAuthor(anwar, anwarFromDao, authorNameToIdMap);
        }
        /**
         * Test a random single book with anywhere name
         */
        {
            Publisher sheba = getAnnoProkash();
            strParam.setParameter(sheba.getName().substring(1, sheba.getName().
                length() - 2));
            strParam.setMatchMode(QueryParameter.MatchMode.ANYWHERE);
            Publisher shebaFromDao = publisherInstance.readSingle(
                Publisher.class,
                strParam);
            assertPublisher(sheba, shebaFromDao,
                publisherNameToIdMap);
        }
        /**
         * Test a random single book with author's name
         */
        {
            Book webDbApp = getWebDbApp(null, null, null);
            QueryParameter<String> authorParam = getAuthorNestedParam();
            Author hughWilliams = getHughWilliams();
            strParam.setParameter(hughWilliams.getName());
            strParam.setMatchMode(QueryParameter.MatchMode.EXACT);
            Hashtable<String, QueryParameter> nestedParams =
                new Hashtable<String, QueryParameter>();
            nestedParams.put("name", strParam);
            authorParam.setNestedParameters(nestedParams);
            Book webDbAppFromDao =
                bookInstance.readSingle(Book.class, authorParam);
            final int numOfAuthors = 2;
            assertBook(webDbApp, webDbAppFromDao, bookNameToIdMap, numOfAuthors);
        }
    }

    /**
     * Test of readOther method, of class AbstractDAO.
     */
    public void testReadOther_Class_QueryParameterArr() {
        System.out.println("readOther");
    }

    /**
     * Test of readOtherList method, of class AbstractDAO.
     */
    public void testReadOtherList_Class_QueryParameterArr() {
        System.out.println("readOtherList");
    }

    /**
     * Test of updateEntity method, of class AbstractDAO.
     */
    public void testUpdateEntity() {
        System.out.println("updateEntity");
    }

    /**
     * Test of readSingle method, of class AbstractDAO.
     */
    public void testReadSingle_Class_Hashtable() {
        System.out.println("readSingleHashTable");
                AbstractDAO<Book> bookInstance = getDaoInstance();
        AbstractDAO<Author> authorInstance = getDaoInstance();
        AbstractDAO<Publisher> publisherInstance = getDaoInstance();
        Map<String, Integer> bookNameToIdMap = new HashMap<String, Integer>();
        Map<String, Integer> publisherNameToIdMap =
            new HashMap<String, Integer>();
        Map<String, Integer> authorNameToIdMap = new HashMap<String, Integer>();
        makeNameToIdMap(bookNameToIdMap, bookInstance, authorNameToIdMap,
            authorInstance, publisherNameToIdMap, publisherInstance);
        /**
         * Test non single request
         */
        try {
            bookInstance.readSingle(Book.class);
            fail("Should not succeed retrieving 1 Book!");
        }
        catch (IllegalArgumentException argumentException) {
        }
        catch (Exception exception) {
            fail(exception.getMessage());
        }
        QueryParameter<Integer> param =
            getIdQueryParam();
        /**
         * Try to load a book with non-existing id
         */
        Book nonExistingBook = bookInstance.readSingle(Book.class, getQueryParamHashtable(param));
        assertNull(nonExistingBook);
        /**
         * Test a random single book with id
         */
        {
            Book kothaoKeoNei = getKothaoKeoNei(null, null);
            int bookId = bookNameToIdMap.get(kothaoKeoNei.getName());
            param.setParameter(bookId);
            Book kothaoKeoNeiFromDao =
                bookInstance.readSingle(Book.class, getQueryParamHashtable(param));
            assertBook(kothaoKeoNei, kothaoKeoNeiFromDao, bookNameToIdMap, 1);
        }
        /**
         * Test a random single publisher with id
         */
        {
            Publisher annoProkash = getAnnoProkash();
            int publisherId = publisherNameToIdMap.get(annoProkash.getName());
            param.setParameter(publisherId);
            Publisher annoProkashFromDao = publisherInstance.readSingle(
                Publisher.class,
                getQueryParamHashtable(param));
            assertPublisher(annoProkash, annoProkashFromDao,
                publisherNameToIdMap);
        }
        /**
         * Test a random single author with id
         */
        {
            Author humayunAhmed = getHumayunAhmed();
            int authorId = authorNameToIdMap.get(humayunAhmed.getName());
            param.setParameter(authorId);
            Author humayunAhmedFromDao = authorInstance.readSingle(Author.class,
                getQueryParamHashtable(param));
            assertAuthor(humayunAhmed, humayunAhmedFromDao, authorNameToIdMap);
        }
        QueryParameter<String> strParam =
            getNameQueryParam();
        /**
         * Test different match modes
         */
        /**
         * Test a random single book with exact name
         */
        {
            Book webDbApp = getWebDbApp(null, null, null);
            String bookName = webDbApp.getName();
            strParam.setParameter(bookName);
            strParam.setMatchMode(QueryParameter.MatchMode.EXACT);
            Book webDbAppFromDao =
                bookInstance.readSingle(Book.class, getQueryParamHashtable(strParam));
            final int numOfAuthors = 2;
            assertBook(webDbApp, webDbAppFromDao, bookNameToIdMap, numOfAuthors);
        }
        /**
         * Test a random single author with start and end match of name
         */
        {
            Author brett = getBrett();
            strParam.setParameter(brett.getName().substring(0, 4));
            strParam.setMatchMode(QueryParameter.MatchMode.START);
            Author brettFromDao = authorInstance.readSingle(Author.class,
                getQueryParamHashtable(strParam));
            assertAuthor(brett, brettFromDao, authorNameToIdMap);
            Author anwar = getKaziAnowarHossain();
            strParam.setParameter(anwar.getName().substring(4));
            strParam.setMatchMode(QueryParameter.MatchMode.END);
            Author anwarFromDao = authorInstance.readSingle(Author.class,
                getQueryParamHashtable(strParam));
            assertAuthor(anwar, anwarFromDao, authorNameToIdMap);
        }
        /**
         * Test a random single book with anywhere name
         */
        {
            Publisher sheba = getAnnoProkash();
            strParam.setParameter(sheba.getName().substring(1, sheba.getName().
                length() - 2));
            strParam.setMatchMode(QueryParameter.MatchMode.ANYWHERE);
            Publisher shebaFromDao = publisherInstance.readSingle(
                Publisher.class,
                getQueryParamHashtable(strParam));
            assertPublisher(sheba, shebaFromDao,
                publisherNameToIdMap);
        }
        /**
         * Test a random single book with author's name
         */
        {
            Book webDbApp = getWebDbApp(null, null, null);
            QueryParameter<String> authorParam = getAuthorNestedParam();
            Author hughWilliams = getHughWilliams();
            strParam.setParameter(hughWilliams.getName());
            strParam.setMatchMode(QueryParameter.MatchMode.EXACT);
            Hashtable<String, QueryParameter> nestedParams =
                new Hashtable<String, QueryParameter>();
            nestedParams.put("name", strParam);
            authorParam.setNestedParameters(nestedParams);
            Book webDbAppFromDao =
                bookInstance.readSingle(Book.class, getQueryParamHashtable(authorParam));
            final int numOfAuthors = 2;
            assertBook(webDbApp, webDbAppFromDao, bookNameToIdMap, numOfAuthors);
        }
    }

    /**
     * Test of readOther method, of class AbstractDAO.
     */
    public void testReadOther_Class_Hashtable() {
        System.out.println("readOther");
    }

    /**
     * Test of readOtherList method, of class AbstractDAO.
     */
    public void testReadOtherList_Class_Hashtable() {
        System.out.println("readOtherList");
    }

    /**
     * Test of readList method, of class AbstractDAO.
     */
    public void testReadList_Class_Hashtable() {
        System.out.println("readList");
    }

    /**
     * Test of readSingle method, of class AbstractDAO.
     */
    public void testReadSingle_Class_List() {
        System.out.println("readSingleList");
                AbstractDAO<Book> bookInstance = getDaoInstance();
        AbstractDAO<Author> authorInstance = getDaoInstance();
        AbstractDAO<Publisher> publisherInstance = getDaoInstance();
        Map<String, Integer> bookNameToIdMap = new HashMap<String, Integer>();
        Map<String, Integer> publisherNameToIdMap =
            new HashMap<String, Integer>();
        Map<String, Integer> authorNameToIdMap = new HashMap<String, Integer>();
        makeNameToIdMap(bookNameToIdMap, bookInstance, authorNameToIdMap,
            authorInstance, publisherNameToIdMap, publisherInstance);
        /**
         * Test non single request
         */
        try {
            bookInstance.readSingle(Book.class);
            fail("Should not succeed retrieving 1 Book!");
        }
        catch (IllegalArgumentException argumentException) {
        }
        catch (Exception exception) {
            fail(exception.getMessage());
        }
        QueryParameter<Integer> param =
            getIdQueryParam();
        /**
         * Try to load a book with non-existing id
         */
        Book nonExistingBook = bookInstance.readSingle(Book.class, getQueryParamList(param));
        assertNull(nonExistingBook);
        /**
         * Test a random single book with id
         */
        {
            Book kothaoKeoNei = getKothaoKeoNei(null, null);
            int bookId = bookNameToIdMap.get(kothaoKeoNei.getName());
            param.setParameter(bookId);
            Book kothaoKeoNeiFromDao =
                bookInstance.readSingle(Book.class, getQueryParamHashtable(param));
            assertBook(kothaoKeoNei, kothaoKeoNeiFromDao, bookNameToIdMap, 1);
        }
        /**
         * Test a random single publisher with id
         */
        {
            Publisher annoProkash = getAnnoProkash();
            int publisherId = publisherNameToIdMap.get(annoProkash.getName());
            param.setParameter(publisherId);
            Publisher annoProkashFromDao = publisherInstance.readSingle(
                Publisher.class,
                getQueryParamHashtable(param));
            assertPublisher(annoProkash, annoProkashFromDao,
                publisherNameToIdMap);
        }
        /**
         * Test a random single author with id
         */
        {
            Author humayunAhmed = getHumayunAhmed();
            int authorId = authorNameToIdMap.get(humayunAhmed.getName());
            param.setParameter(authorId);
            Author humayunAhmedFromDao = authorInstance.readSingle(Author.class,
                param);
            assertAuthor(humayunAhmed, humayunAhmedFromDao, authorNameToIdMap);
        }
        QueryParameter<String> strParam =
            getNameQueryParam();
        /**
         * Test different match modes
         */
        /**
         * Test a random single book with exact name
         */
        {
            Book webDbApp = getWebDbApp(null, null, null);
            String bookName = webDbApp.getName();
            strParam.setParameter(bookName);
            strParam.setMatchMode(QueryParameter.MatchMode.EXACT);
            Book webDbAppFromDao =
                bookInstance.readSingle(Book.class, getQueryParamHashtable(strParam));
            final int numOfAuthors = 2;
            assertBook(webDbApp, webDbAppFromDao, bookNameToIdMap, numOfAuthors);
        }
        /**
         * Test a random single author with start and end match of name
         */
        {
            Author brett = getBrett();
            strParam.setParameter(brett.getName().substring(0, 4));
            strParam.setMatchMode(QueryParameter.MatchMode.START);
            Author brettFromDao = authorInstance.readSingle(Author.class,
                getQueryParamHashtable(strParam));
            assertAuthor(brett, brettFromDao, authorNameToIdMap);
            Author anwar = getKaziAnowarHossain();
            strParam.setParameter(anwar.getName().substring(4));
            strParam.setMatchMode(QueryParameter.MatchMode.END);
            Author anwarFromDao = authorInstance.readSingle(Author.class,
                getQueryParamHashtable(strParam));
            assertAuthor(anwar, anwarFromDao, authorNameToIdMap);
        }
        /**
         * Test a random single book with anywhere name
         */
        {
            Publisher sheba = getAnnoProkash();
            strParam.setParameter(sheba.getName().substring(1, sheba.getName().
                length() - 2));
            strParam.setMatchMode(QueryParameter.MatchMode.ANYWHERE);
            Publisher shebaFromDao = publisherInstance.readSingle(
                Publisher.class,
                getQueryParamHashtable(strParam));
            assertPublisher(sheba, shebaFromDao,
                publisherNameToIdMap);
        }
        /**
         * Test a random single book with author's name
         */
        {
            Book webDbApp = getWebDbApp(null, null, null);
            QueryParameter<String> authorParam = getAuthorNestedParam();
            Author hughWilliams = getHughWilliams();
            strParam.setParameter(hughWilliams.getName());
            strParam.setMatchMode(QueryParameter.MatchMode.EXACT);
            Hashtable<String, QueryParameter> nestedParams =
                new Hashtable<String, QueryParameter>();
            nestedParams.put("name", strParam);
            authorParam.setNestedParameters(nestedParams);
            Book webDbAppFromDao =
                bookInstance.readSingle(Book.class, getQueryParamHashtable(authorParam));
            final int numOfAuthors = 2;
            assertBook(webDbApp, webDbAppFromDao, bookNameToIdMap, numOfAuthors);
        }
    }

    /**
     * Test of readOther method, of class AbstractDAO.
     */
    public void testReadOther_Class_List() {
        System.out.println("readOther");
    }

    /**
     * Test of readOtherList method, of class AbstractDAO.
     */
    public void testReadOtherList_Class_List() {
        System.out.println("readOtherList");
    }

    /**
     * Test of readList method, of class AbstractDAO.
     */
    public void testReadList_Class_List() {
        System.out.println("readList");
    }

    /**
     * Test of deleteEntity method, of class AbstractDAO.
     */
    public void testDeleteEntity() {
        System.out.println("deleteEntity");
    }

    private void assertAuthor(final Author author,
                              final Author authorFromDao,
                              final Map<String, Integer> authorNameToIdMap) {
        assertNotNull(authorFromDao);
        assertEquals(authorNameToIdMap.get(author.getName()).intValue(),
            authorFromDao.getId().intValue());
        assertEquals(author.getName(), authorFromDao.getName());
    }

    private void assertBook(final Book book,
                            final Book bookFromDao,
                            final Map<String, Integer> bookNameToIdMap,
                            final int numOfAuthors) {
        assertNotNull(bookFromDao);
        assertEquals(bookNameToIdMap.get(book.getName()).intValue(),
            bookFromDao.getId().intValue());
        assertEquals(book.getName(), bookFromDao.getName());
        assertEquals(book.getIsbn(), bookFromDao.getIsbn());
        if (numOfAuthors > -1) {
            assertEquals(numOfAuthors, bookFromDao.getAuthors().size());
        }
    }

    private void assertPublisher(final Publisher publisher,
                                 final Publisher publisherFromDao,
                                 final Map<String, Integer> publisherNameToIdMap) {
        assertNotNull(publisherFromDao);
        assertEquals(publisherNameToIdMap.get(publisher.getName()).intValue(),
            publisherFromDao.getId().intValue());
        assertEquals(publisher.getName(), publisherFromDao.getName());
        assertEquals(publisher.getNumOfEmployees(),
            publisherFromDao.getNumOfEmployees());
    }

    private void enterBookToIndex(List<Book> bookList,
                                  Book searchedBook,
                                  Map<String, Integer> bookNameToIdMap) {
        int index = getBookIndex(bookList, searchedBook);
        assertTrue(index > -1);
        bookNameToIdMap.put(searchedBook.getName(), bookList.get(index).getId());
        bookList.remove(index);
    }

    private void enterAuthorToIndex(List<Author> authorList,
                                    Author searchedAuthor,
                                    Map<String, Integer> authorNameToIdMap) {
        int index = getAuthorIndex(authorList, searchedAuthor);
        assertTrue(index > -1);
        authorNameToIdMap.put(searchedAuthor.getName(), authorList.get(index).
            getId());
        authorList.remove(index);
    }

    private void enterPublisherToIndex(List<Publisher> publisherList,
                                       Publisher searchedPublisher,
                                       Map<String, Integer> publisherNameToIdMap) {
        int index = getPublisherIndex(publisherList, searchedPublisher);
        assertTrue(index > -1);
        publisherNameToIdMap.put(searchedPublisher.getName(), publisherList.get(
            index).getId());
        publisherList.remove(index);
    }

    private Book getAgniSopoth(Author author,
                               Publisher publisher) {
        final String bookName = "Agni Sopoth";
        final Date publishDate = new Date();
        final String isbn = "123ABC";
        return getBook(publisher, bookName, publishDate, isbn, author);
    }

    private Book getAgunerPoroshMoni(Publisher annoProkash,
                                     Author humayunAhmed) {
        return getBook(annoProkash, "Aguner Poroshmoni", new Date(), "222VFEE3",
            humayunAhmed);
    }

    private <Template extends PersistentDTO<Template>> Set<Template> getAll(
        final AbstractDAO<Template> bookInstance,
        final Class<Template> templateClass) {
        return new LinkedHashSet<Template>(bookInstance.readList(templateClass));
    }

    private Publisher getAnnoProkash() {
        return getPublisher(new Date(), 50, "Anno Prokash");
    }

    private QueryParameter<String> getAuthorNestedParam() {
        return new QueryParameter<String>("authors",
            QueryParameter.PARAMETER_TYPE_NESTED_PROPERTY,
            QueryParameter.OPERATOR_EQUAL, "");
    }

    private int getBookIndex(final List<Book> bookList,
                             final Book searchedBook) {
        int index;
        for (index = 0; index < bookList.size();
            ++index) {
            Book book = bookList.get(index);
            if (searchedBook.getName().equals(book.getName())) {
                break;
            }
        }
        return index;
    }

    private Hashtable<String, QueryParameter> getQueryParamHashtable(QueryParameter... params) {
        Hashtable<String, QueryParameter> table = new Hashtable<String, QueryParameter>();
        for(QueryParameter parameter : params) {
            String paramName = parameter.getPropertyName();
            if(table.containsKey(paramName)) {
                int i = 1;
                while(table.containsKey(paramName + i)) {
                    i++;
                }
                paramName = paramName + i;
            }
            table.put(paramName, parameter);
        }
        return table;
    }

    private List<QueryParameter> getQueryParamList(QueryParameter... params) {
        ArrayList<QueryParameter> result = new ArrayList<QueryParameter>();
        Collections.addAll(result, params);
        return result;
    }

    private QueryParameter<Integer> getIdQueryParam() {
        return new QueryParameter<Integer>("id",
            QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_EQUAL, -1);
    }

    private QueryParameter<String> getNameQueryParam() {
        return new QueryParameter<String>("name",
            QueryParameter.PARAMETER_TYPE_PROPERTY,
            QueryParameter.OPERATOR_STRING_LIKE, "");
    }

    private int getPublisherIndex(final List<Publisher> publisherList,
                                  final Publisher searchedBook) {
        int index;
        for (index = 0; index < publisherList.size();
            ++index) {
            Publisher publisher = publisherList.get(index);
            if (searchedBook.getName().equals(publisher.getName())) {
                break;
            }
        }
        return index;
    }

    private int getAuthorIndex(final List<Author> authorList,
                               final Author searchedBook) {
        int index;
        for (index = 0; index < authorList.size();
            ++index) {
            Author author = authorList.get(index);
            if (searchedBook.getName().equals(author.getName())) {
                break;
            }
        }
        return index;
    }

    private Author getBrett() {
        return getAuthor("Brett McLaugblin", new Date());
    }

    private Author getDavidLane() {
        return getAuthor("David Lane", new Date());
    }

    private Author getHughWilliams() {
        return getAuthor("Hugh E. Williams", new Date());
    }

    private Author getHumayunAhmed() {
        return getAuthor("Humayun Ahmed", new Date());
    }

    private Book getJavaAndXml(Publisher oReilly,
                               Author brettMcLaugblin) {
        return getBook(oReilly, "Java & XML", new Date(), "555UIP66",
            brettMcLaugblin);
    }

    private Author getKaziAnowarHossain() {
        final String name = "Kazi Anowar Hossain";
        final Date birthDate = new Date();
        return getAuthor(name, birthDate);
    }

    private Book getKothaoKeoNei(Publisher annoProkash,
                                 Author humayunAhmed) {
        return getBook(annoProkash, "Kothao Keo Nei", new Date(), "11134BCE",
            humayunAhmed);
    }

    private Publisher getOReilly() {
        return getPublisher(new Date(), 110, "O\'Reilly");
    }

    private Publisher getShebaProkashani() {
        final String name = "Sheba Prokashoni";
        final Date establishDate = new Date();
        final int numOfEmployees = 100;
        return getPublisher(establishDate, numOfEmployees, name);
    }

    private Publisher getPublisher(final Date establishDate,
                                   final int numOfEmployees,
                                   final String name) {
        Publisher publisher =
            new Publisher();
        publisher.setEstablishedDate(establishDate);
        publisher.setNumOfEmployees(numOfEmployees);
        publisher.setName(name);
        return publisher;
    }

    private Author getAuthor(final String name,
                             final Date birthDate) {
        Author author =
            new Author();
        author.setName(name);
        author.setBirthDate(birthDate);
        return author;
    }

    private Book getBook(final Publisher publisher,
                         final String bookName,
                         final Date publishDate,
                         final String isbn,
                         final Author... authors) {
        Book book = new Book();
        Set<Author> authorSet =
            new HashSet<Author>();
        for (Author author : authors) {
            authorSet.add(author);
        }
        book.setAuthors(authorSet);
        book.setPublisher(publisher);
        book.setName(bookName);
        book.setPublishDate(publishDate);
        book.setIsbn(isbn);
        return book;
    }

    private <T extends PersistentDTO<T>> AbstractDAO<T> getDaoInstance()
        throws BeansException {
        AbstractDAO<T> instance =
            (AbstractDAO<T>) context.getBean("testDao");
        assertNotNull(instance);
        return instance;
    }

    private Book getWebDbApp(Publisher oReilly,
                             Author davidLane,
                             Author hughWilliams) {
        return getBook(oReilly, "Web Database Applications", new Date(),
            "444ERT6", davidLane, hughWilliams);
    }

    private void makeNameToIdMap(Map<String, Integer> bookNameToIdMap,
                                 AbstractDAO<Book> bookInstance,
                                 Map<String, Integer> authorNameToIdMap,
                                 AbstractDAO<Author> authorInstance,
                                 Map<String, Integer> publisherNameToIdMap,
                                 AbstractDAO<Publisher> publisherInstance) {
        /**
         * Map Books
         */
        Set<Book> books = getAll(bookInstance, Book.class);
        List<Book> bookList = new ArrayList<Book>(books);
        Book kothaoKeoNei = getKothaoKeoNei(null, null);
        enterBookToIndex(bookList, kothaoKeoNei, bookNameToIdMap);
        Book agunerPoroshMoni = getAgunerPoroshMoni(null, null);
        enterBookToIndex(bookList, agunerPoroshMoni, bookNameToIdMap);
        Book webDbApp = getWebDbApp(null, null, null);
        enterBookToIndex(bookList, webDbApp, bookNameToIdMap);
        Book javaAndXml = getJavaAndXml(null, null);
        enterBookToIndex(bookList, javaAndXml, bookNameToIdMap);
        Book agniSopoth = getAgniSopoth(null, null);
        enterBookToIndex(bookList, agniSopoth, bookNameToIdMap);
        /**
         * Map Publishers
         */
        Set<Publisher> publishers = getAll(publisherInstance, Publisher.class);
        List<Publisher> publisherList = new ArrayList<Publisher>(publishers);
        Publisher shebaProkashani = getShebaProkashani();
        enterPublisherToIndex(publisherList, shebaProkashani,
            publisherNameToIdMap);
        Publisher oReilly = getOReilly();
        enterPublisherToIndex(publisherList, oReilly, publisherNameToIdMap);
        Publisher annoProkash = getAnnoProkash();
        enterPublisherToIndex(publisherList, annoProkash, publisherNameToIdMap);
        /**
         * Map Authors
         */
        Set<Author> authors = getAll(authorInstance, Author.class);
        List<Author> authorList = new ArrayList<Author>(authors);
        Author kaziAnowarHossain = getKaziAnowarHossain();
        enterAuthorToIndex(authorList, kaziAnowarHossain, authorNameToIdMap);
        Author brettMcLaugblin = getBrett();
        enterAuthorToIndex(authorList, brettMcLaugblin, authorNameToIdMap);
        Author davidLane = getDavidLane();
        enterAuthorToIndex(authorList, davidLane, authorNameToIdMap);
        Author hughWilliams = getHughWilliams();
        enterAuthorToIndex(authorList, hughWilliams, authorNameToIdMap);
        Author humayunAhmed = getHumayunAhmed();
        enterAuthorToIndex(authorList, humayunAhmed, authorNameToIdMap);

    }
}