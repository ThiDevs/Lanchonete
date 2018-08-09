package alves.thiago.lanchonete;

/**
 * @author Alhaytham Alfeel on 10/10/2016.
 */

public class CardModel {
    private int imageId;
    private String titleId;
    private String subtitleId;

    public CardModel(String titleId, String subtitleId) {
        //this.imageId = imageId;
        this.titleId = titleId;
        this.subtitleId = subtitleId;
    }

   // public int getImageId() {
  //      return imageId;
  //  }

    public String getTitle() {
        return titleId;
    }

    public String getSubtitle() {
        return subtitleId;
    }
}
