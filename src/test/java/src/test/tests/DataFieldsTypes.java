package src.test.tests;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DataFieldsTypes {

    public String textField(String DDStextField, Document document){

        // Use JSoup to select the input element with the specified data-drupal-selector attribute
        Elements inputElements = document.select("input[data-drupal-selector=" + DDStextField + "]");


        if (!inputElements.isEmpty()) {


            // Get the value of the input element
            return inputElements.attr("value");

        }
        return null; // Return null if the input element is not found
    }

    public String textFieldBody(String DDStextFieldBody, Document document){


        // Use JSoup to select the textarea element with the specified data-drupal-selector attribute
        Elements textAreaElements = document.select("textarea[data-drupal-selector=" + DDStextFieldBody + "]");
        if (!textAreaElements.isEmpty()) {
            // Get the value of the data-editor-value-original attribute
            String originalValue = textAreaElements.first().attr("data-editor-value-original");

            // Use JSoup to parse the HTML content
            Document parsedDoc = Jsoup.parse(originalValue);

            // Remove HTML tags from the parsed content
            String cleanedContent = parsedDoc.text();

            return cleanedContent;
        }
        return null; // Return null if the textarea element is not found
    }

    public String radioButton(String DDSradioButton, Document document){
        // Use JSoup to select the fieldset element with the specified data-drupal-selector attribute
        Element fieldset = document.selectFirst("fieldset[data-drupal-selector=" + DDSradioButton + "]");

        // Check if the fieldset element is found
        if (fieldset != null) {
            // Find all radio button elements within the fieldset
            Elements radioButtons = fieldset.select("input[type=radio]");

            // Iterate over the radio buttons to find the checked one
            for (Element radioButton : radioButtons) {
                // Check if the current radio button is checked
                if (radioButton.hasAttr("checked")) {
                    // Get the corresponding label for the checked radio button
                    Element labelElement = fieldset.selectFirst("label[for=" + radioButton.id() + "]");
                    if (labelElement != null) {
                        return labelElement.text();
                    }
                }
            }
        }

        return null; // Return null if the specified radio button is not found or if no button is checked
    }

    public String dropDown(String DDSDropDown, Document document){

        // Use JSoup to select the select element with the specified data-drupal-selector attribute
        Element selectElement = document.selectFirst("select[data-drupal-selector=" + DDSDropDown + "]");

        // Check if the select element is found
        if (selectElement != null) {
            // Get the selected option
            Element selectedOption = selectElement.selectFirst("option[selected]");

            // Check if a selected option is found
            if (selectedOption != null) {
                // Return the text content of the selected option
                return selectedOption.text();
            }
        }

        return null; // Return null if the specified select element or selected option is not found
    }

    public String textFieldDescription(String DDStextFieldDescription, Document document){


        // Use JSoup to select the textarea element with the specified data-drupal-selector attribute
        Element textareaElement = document.selectFirst("textarea[data-drupal-selector=" + DDStextFieldDescription + "]");

        // Check if the textarea element is found
        if (textareaElement != null) {
            // Get the text content of the textarea
            String text = textareaElement.text();
            return text;
        }

        return null; // Return null if the textarea element is not found
    }

    public String mediaEntity(String DDSMedia, Document document){

        // Use JSoup to select the div element with the specified data-drupal-selector attribute
        Element divElement = document.selectFirst("div[data-drupal-selector=" + DDSMedia + "]");

        // Check if the div element is found
        if (divElement != null) {
            // Get the text content of the <a> element within the div
            Element aElement = divElement.selectFirst("a");
            if (aElement != null) {
                String linkText = aElement.text();
                return linkText;
            }
        }

        return null; // Return null if the div element or <a> element is not found
    }

    public String checkBox(String DDSCheckBox, Document document){

        // Use JSoup to select the checkbox element with the specified data-drupal-selector attribute
        Element checkboxElement = document.selectFirst("input[type=checkbox][data-drupal-selector=" + DDSCheckBox + "]");

        // Check if the checkbox element is found
        if (checkboxElement != null) {
            // Check if the 'checked' attribute is present
            boolean isChecked = checkboxElement.hasAttr("checked");
            // Return "True" if the checkbox is selected, otherwise return "False"
            return String.valueOf(isChecked);

        }

        // If the checkbox is not checked or not found, return "False"
        return "False";
    }

    public String staticFieldLink(String DDSFieldLink, Document document){

        // Use JSoup to select the input element with the specified data-drupal-selector attribute
        Elements inputElements = document.select("input[data-drupal-selector=" + DDSFieldLink + "]");


        if (!inputElements.isEmpty()) {


            // Get the value of the input element
            return inputElements.attr("value");

        }

        return null; // Return null if the input element is not found



    }

    public String multipleItem(String DDSMultipleItem, Document document){

        StringBuilder valuesBuilder = new StringBuilder(); // StringBuilder to store values
        String cellData = "" ;
        // Start with index 0 and increment until a null value is encountered
        int index = 0;
        while (true) {
            // Construct the current data-drupal-selector with the index
            String currentSelector = DDSMultipleItem.replace("$", String.valueOf(index));
            // Select the input element using the current selector
            Element inputElement = document.selectFirst("input[data-drupal-selector=" + currentSelector + "]");

            // Check if the input element is found
            if (inputElement != null) {
                // Get the value attribute of the input element
                String value = inputElement.attr("value");

                // Append the value to the StringBuilder with a space if it's not empty
                if (!value.isEmpty()) {
                    valuesBuilder.append(value).append(" ");
                    System.out.println(valuesBuilder);
                }
            } else {
                // Exit the loop if the input element is not found
                return valuesBuilder.toString().trim();

            }

            // Increment the index for the next iteration
            index++;
        }





    }


}
