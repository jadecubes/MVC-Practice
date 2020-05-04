package jrails;

import books.Book;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ModelTest {

    private Book b;

    @Before
    public void setUp() throws Exception {
        b = new Book();
        b.title = "Programming Languages: Build, Prove, and Compare";
        b.author = " ";
        b.num_copies = 999;
        b.isThick=true;
    }
    @Test
    public void id() {
        assertThat(b.id(), notNullValue());
    }
    @Test
    public void save() {
        assertThat(b.id(), is(0));
        b.save();
        File file = new File("models/Book.db");
        assertTrue(file.exists());
        assertThat(b.id(), not(0));
    }
    @Test
    public void find() {
        assertThat(Model.find(Book.class,0),nullValue());
        b.save();
        assertThat(Model.find(Book.class,b.id()),notNullValue());
    }
    @Test
    public void destroy() {
        b.save();
        b.destroy();
        File file = new File("models/Book.db");
        assertTrue(file.exists());
        assertThat(Model.find(Book.class,b.id()),nullValue());
        //destroy();
    }
    @Test
    public void all() {
        b.save();
        List list=Model.all(Book.class);
        assertThat(list.size(),equalTo(1));
        assertThat(((Model)list.get(0)).id(),not(0));
    }
    @After
    public void tearDown()  {
        Model.reset();
        File file = new File("models");
        assertTrue(!file.exists());
    }
}