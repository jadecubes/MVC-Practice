package jrails;

public class View {
    private static Html html=new Html("");
    public static Html empty() {
        return html.empty();
    }

    public static Html br() {
        return html.br();
    }

    public static Html t(Object o) {
        // Use o.toString() to get the text for this
        return html.t(o);
    }

    public static Html p(Html child) {
        return html.p(child);
    }

    public static Html div(Html child) {
        return html.div(child);
    }

    public static Html strong(Html child) {
        return html.strong(child);
    }

    public static Html h1(Html child) {
        return html.h1(child);
    }

    public static Html tr(Html child) {
        return html.tr(child);
    }

    public static Html th(Html child) {
        return html.th(child);
    }

    public static Html td(Html child) {
        return html.td(child);
    }

    public static Html table(Html child) {
        return html.table(child);
    }

    public static Html thead(Html child) {
        return html.thead(child);
    }

    public static Html tbody(Html child) {
        return html.tbody(child);
    }

    public static Html textarea(String name, Html child) {
        return html.textarea(name,child);
    }

    public static Html link_to(String text, String url) {
        return html.link_to(text,url);
    }

    public static Html form(String action, Html child) {
        return html.form(action,child);
    }

    public static Html submit(String value) {
        return html.submit(value);
    }
}