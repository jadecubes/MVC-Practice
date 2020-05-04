package jrails;

public class Html {
    private String html="";
    public Html(){}
    public Html(String htmlCode){
        if(htmlCode!=null)
            html=htmlCode;
    }

    public String toString() {
        return html;
    }

    public Html empty() {
        return new Html("");
    }

    public Html seq(Html h) {
        return new Html(toString()+h.toString());
    }

    public Html br() {
        return new Html("<br/>");
    }

    public Html t(Object o) {
        // Use o.toString() to get the text for this
        if(o==null)
            throw new UnsupportedOperationException();
        else
            return new Html(toString()+o.toString());
    }

    public Html p(Html child) {
        return new Html("<p>"+child.toString()+"</p>");
    }

    public Html div(Html child) {
        return new Html("<div>"+child.toString()+"</div>");
    }

    public Html strong(Html child) {
        return new Html("<strong>"+child.toString()+"</strong>");
    }

    public Html h1(Html child) {
        return new Html("<h1>"+child.toString()+"</h1>");
    }

    public Html tr(Html child) {
        return new Html("<tr>"+child.toString()+"</tr>");
    }

    public Html th(Html child) {
        return new Html("<th>"+child.toString()+"</th>");
    }

    public Html td(Html child) {
        return new Html("<td>"+child.toString()+"</td>");
    }

    public Html table(Html child) {
        return new Html("<table>"+child.toString()+"</table>");
    }

    public Html thead(Html child) {
        return new Html("<thead>"+child.toString()+"</thead>");
    }

    public Html tbody(Html child) {
        return new Html("<tbody>"+child.toString()+"</tbody>");
    }

    public Html textarea(String name, Html child) {
        return new Html("<textarea name=\""+name+"\">"+child.toString()+"</textarea>");
    }

    public Html link_to(String text, String url) {
        return new Html("<a href=\""+ url+"\">"+text+"</a>");
    }

    public Html form(String action, Html child) {
        return new Html("<form action=\""+action+"\""+ " accept-charset=\"UTF-8\" method=\"post\">"+ child.toString()+"</form>");
    }

    public Html submit(String value) {
        return new Html("<input type=\"submit\" value=\""+value+"\"/>");
    }
}