package jrails;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class HtmlTest {

    private Html html;
    private String sampleTxt="Hello!";
    @Before
    public void setUp() throws Exception {
        html = new Html();
    }
    @Test
    public void empty() {
        assertThat(html.empty().toString(), isEmptyString());
        html=new Html(null);
        assertThat(html.empty().toString(), isEmptyString());
    }
    @Test
    public void t() {
        assertThat(html.t(sampleTxt).toString(), is(sampleTxt));
    }
    @Test
    public void br() {
        assertThat(html.t(sampleTxt).seq(html.br()).toString(), is(sampleTxt+"<br/>"));
    }
    @Test
    public void seq() {
        assertThat(html.seq(html.br()).toString(), is("<br/>"));
    }
    @Test
    public void p() {
        assertThat(html.p(html.t(sampleTxt)).toString(), is("<p>"+sampleTxt+"</p>"));
    }
    @Test
    public void strong() {
        assertThat(html.strong(html.t(sampleTxt)).toString(), is("<strong>"+sampleTxt+"</strong>"));
    }
    @Test
    public void h1() {
        assertThat(html.h1(html.t(sampleTxt)).toString(), is("<h1>"+sampleTxt+"</h1>"));
    }
    @Test
    public void tr() {
        Html thstr=html.th(html.t(sampleTxt));
        Html tdstr=html.td(html.t(sampleTxt));
        assertThat(html.tr(thstr).seq(html.tr(tdstr)).toString(), is("<tr>"+thstr+"</tr><tr>"+tdstr+"</tr>"));
    }
    @Test
    public void th() {
        assertThat(html.th(html.t(sampleTxt)).toString(), is("<th>"+sampleTxt+"</th>"));
    }
    @Test
    public void td() {
        assertThat(html.td(html.t(sampleTxt)).toString(), is("<td>"+sampleTxt+"</td>"));
    }
    @Test
    public void table() {
        /*Html thstr=html.th(html.t(sampleTxt));
        Html tdstr=html.td(html.t(sampleTxt));
        assertThat(html.table(html.tr(thstr).seq(html.tr(tdstr))).toString(), is("<table><tr>"+thstr+"</tr><tr>"+tdstr+"</tr></table>"));*/
        assertThat(html.table(html).toString(), is("<table></table>"));
    }
    @Test
    public void thead() {
       /* Html thstr=html.th(html.t(sampleTxt));
        Html tdstr=html.td(html.t(sampleTxt));
        assertThat(html.table(html.thead(html.tr(thstr)).seq(html.tbody(html.tr(tdstr)))).toString(), is("<table><thead><tr>"+thstr+"</tr></thead><tbody><tr>"+tdstr+"</tr></tbody></table>"));*/
        assertThat(html.thead(html).toString(), is("<thead></thead>"));
    }
    @Test
    public void tbody() {
        assertThat(html.tbody(html).toString(), is("<tbody></tbody>"));
    }
    @Test
    public void textarea() {
        assertThat(html.textarea(sampleTxt,html).toString(), is("<textarea name=\""+sampleTxt+"\"></textarea>"));
    }
    @Test
    public void link_to() {
        assertThat(html.link_to(sampleTxt,sampleTxt).toString(), is("<a href=\""+ sampleTxt+"\">"+sampleTxt+"</a>"));
    }
    @Test
    public void form() {
        assertThat(html.form(sampleTxt,html).toString(), is("<form action=\""+sampleTxt+"\""+" accept-charset=\"UTF-8\" method=\"post\">"+"</form>"));
    }
    @Test
    public void submit() {
        assertThat(html.submit(sampleTxt).toString(), is("<input type=\"submit\" value=\""+sampleTxt+"\"/>"));
    }
    @Test
    public void div() {
        assertThat(html.div(html).toString(), is("<div></div>"));
    }

}