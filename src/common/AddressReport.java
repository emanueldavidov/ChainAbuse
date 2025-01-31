package common;

import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;

/**
 * Represents a report with details such as an address, report count, 
 * an optional image, and a hyperlink.
 */
public class AddressReport {

    private String address;      // The address associated with the report
    private String reportCount;  // Number of reports related to the address
    private ImageView img;       // Optional image for the report
    private Hyperlink link;      // Hyperlink associated with the report

 
    // Constructs a AddressReport object with the given details.
   
    public AddressReport(String address, String report_count, String link) {
        this.address = address;
        this.reportCount = report_count;
        this.img = null;
        this.link = new Hyperlink(link);
    }

    // Getter and setter for address
    public String getAddress() {
        return address;
    }

    public void setAddress(String address_) {
        address = address_;
    }

    
    public String getReportCount() {
        return reportCount;
    }

    public void setReportCount(String reportCount_) {
        reportCount = reportCount_;
    }

    
    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img_) {
        img = img_;
    }


    public Hyperlink getLink() {
        return link;
    }

    public void setLink(String link_) {
        link = new Hyperlink(link_);
    }
}
