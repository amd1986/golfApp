package fr.greta.golf.model;

public enum HtmlTags {
    TITLE("h1"), DIVISION("div"), PARAGRAPH("p"), ULIST("ul"), OLIST("ol"), ILIST("li"), SPAN("span");

    private String tagName;

    HtmlTags(String tag) {
        this.tagName = tag;
    }

    public String getTagName() {
        return tagName;
    }
}
