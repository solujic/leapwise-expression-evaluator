package hr.solujic.leapwise.util;

import com.fasterxml.jackson.databind.JsonNode;

public class JsonUtil {

  /***
   * Returns true if object on this path exists, otherwise returns false.
   * @param jsonObject - json object.
   * @param path - path to the object to check.
   * @return
   */
  public static boolean checkIfJsonObjectExists(JsonNode jsonObject, String path) {
    JsonNode previousNode = null;
    var pathArray = path.split("\\.");

    for (String s : pathArray) {
      if (previousNode == null) {
        previousNode = jsonObject.path(s);
      } else {
        previousNode = previousNode.path(s);
      }
    }

    if (previousNode == null) {
      previousNode = jsonObject;
    }

    return previousNode.isObject();
  }

  /***
   * Returns the value of the JSON field for the provided JSON object and the path.
   * @param jsonObject - json object.
   * @param path - path to the field using the dots "." for navigating the objects (example: nestedObject.fieldName).
   * @return
   */
  public static Object getFieldValueForPath(JsonNode jsonObject, String path) {
    JsonNode previousNode = null;
    var pathArray = path.split("\\.");
    String value = "";

    for (int i = 0; i < pathArray.length; i++) {
      if (previousNode == null) {
        previousNode = jsonObject.path(pathArray[i]);
      } else {
        previousNode = previousNode.path(pathArray[i]);
      }

      if (i == pathArray.length - 1) {
        value = previousNode.asText();
      }
    }

    return value;
  }
}
