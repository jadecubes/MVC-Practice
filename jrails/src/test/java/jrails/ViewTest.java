package jrails;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class ViewTest {
    private String sampleTxt="Hello!";
    @Test
    public void empty() {
        assertThat(View.empty().toString(), isEmptyString());
    }
    @Test
    public void t() {
        assertThat(View.t(sampleTxt).toString(), is(sampleTxt));
    }
    @Test
    public void br() {
        assertThat(View.t(sampleTxt).seq(View.br()).toString(), is(sampleTxt+"<br/>"));
    }
    @Test
    public void p() {
        assertThat(View.p(View.t(sampleTxt)).toString(), is("<p>"+sampleTxt+"</p>"));
    }
    @Test
    public void strong() {
        assertThat(View.strong(View.t(sampleTxt)).toString(), is("<strong>"+sampleTxt+"</strong>"));
    }
    @Test
    public void h1() {
        assertThat(View.h1(View.t(sampleTxt)).toString(), is("<h1>"+sampleTxt+"</h1>"));
    }
    @Test
    public void tr() {
        Html thstr=View.th(View.t(sampleTxt));
        Html tdstr=View.td(View.t(sampleTxt));
        assertThat(View.tr(thstr).seq(View.tr(tdstr)).toString(), is("<tr>"+thstr+"</tr><tr>"+tdstr+"</tr>"));
    }
    @Test
    public void th() {
        assertThat(View.th(View.t(sampleTxt)).toString(), is("<th>"+sampleTxt+"</th>"));
    }
    @Test
    public void td() {
        assertThat(View.td(View.t(sampleTxt)).toString(), is("<td>"+sampleTxt+"</td>"));
    }
    @Test
    public void table() {
        assertThat(View.table(View.t(sampleTxt)).toString(), is("<table>"+sampleTxt+"</table>"));
    }
    @Test
    public void thead() {
        assertThat(View.thead(View.t(sampleTxt)).toString(), is("<thead>"+sampleTxt+"</thead>"));
    }
    @Test
    public void tbody() {
        assertThat(View.tbody(View.t(sampleTxt)).toString(), is("<tbody>"+sampleTxt+"</tbody>"));
    }
    @Test
    public void textarea() {
        assertThat(View.textarea(sampleTxt,View.t(sampleTxt)).toString(), is("<textarea name=\""+sampleTxt+"\">"+sampleTxt+"</textarea>"));
    }
    @Test
    public void link_to() {
        assertThat(View.link_to(sampleTxt,sampleTxt).toString(), is("<a href=\""+ sampleTxt+"\">"+sampleTxt+"</a>"));
    }
    @Test
    public void form() {
        assertThat(View.form(sampleTxt,View.t(sampleTxt)).toString(), is("<form action=\""+sampleTxt+"\" accept-charset=\"UTF-8\" method=\"post\">"+sampleTxt+"</form>"));
    }
    @Test
    public void submit() {
        assertThat(View.submit(sampleTxt).toString(), is("<input type=\"submit\" value=\""+sampleTxt+"\"/>"));
    }
    @Test
    public void div() {
        assertThat(View.div(View.t(sampleTxt)).toString(), is("<div>"+sampleTxt+"</div>"));
    }

}