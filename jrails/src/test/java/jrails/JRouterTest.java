package jrails;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import books.BookController;
import books.BookView;
import org.junit.*;


public class JRouterTest {

    private JRouter jRouter;

    @Before
    public void setUp() throws Exception {
        jRouter = new JRouter();
    }

    @Test
    public void addRoute() {
        assertThat(jRouter.getRoute("GET","/show"),nullValue());
        jRouter.addRoute("GET", "/show", BookController.class, "show");
        assertThat(jRouter.getRoute("GET","/show"),is("BookController#show"));
    }
    @Test
    public void getRoute(){
        assertThat(jRouter.getRoute("GET","/show"),nullValue());
        jRouter.addRoute("GET", "/show", BookController.class, "show");
        assertThat(jRouter.getRoute("GET","/show"),is("BookController#show"));
    }
    @Test
    public void route() {
        jRouter.addRoute("GET", "/new_book", BookController.class, "new_book");
        assertThat(jRouter.route("GET","/new_book",null).toString(),is(BookController.new_book(null).toString()));
    }
}