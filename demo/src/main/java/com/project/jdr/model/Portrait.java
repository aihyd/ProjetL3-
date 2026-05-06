package com.project.jdr.model;

public class Portrait extends ElementFiche {

    private String cheminImage;

    public Portrait(String cheminImage) {
        super();
        this.cheminImage = cheminImage;
        this.width = 150;
        this.height = 150;
    }

    public Portrait(int id, String cheminImage, int x, int y, double width, double height) {
        super(id, x, y, width, height);
        this.cheminImage = cheminImage;
    }

    public String getCheminImage() {
        return cheminImage;
    }

    public void setCheminImage(String cheminImage) {
        if (cheminImage != null && !cheminImage.trim().isEmpty()) {
            this.cheminImage = cheminImage;
        }
    }
}