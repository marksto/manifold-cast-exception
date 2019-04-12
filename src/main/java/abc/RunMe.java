package abc;

import abc.schema.*;
import manifold.api.json.IJsonList;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

public class RunMe {
  static File single_item;
  static File items_array;

  public static void main(String[] args) throws URISyntaxException {
    ClassLoader classLoader = RunMe.class.getClassLoader();
    single_item = new File(classLoader.getResource("abc.data/single_item.json").toURI());
    items_array = new File(classLoader.getResource("abc.data/items_array.json").toURI());

    // successful pass is a precondition
    loadSingleItem();

    loadItemsArray_PlainInterface();
    loadItemsArray_ExtraMediateType();
  }

  private static void loadSingleItem() {
    Item item = Item.load().fromJsonFile(single_item);
    System.out.println(item.write().toJson());
  }

  private static void loadItemsArray_PlainInterface() {
    List<Item> items = IJsonList.<Item>load().fromJsonFile(items_array);

    // should pass
    items.forEach(item -> System.out.println(item.write().toJson()));
    items.forEach(item -> System.out.println(item.getId()));

    // fails
    items.stream().map(Item::getId).forEach(System.out::println);
  }

  private static void loadItemsArray_ExtraMediateType() {
    List<Item> items = ItemsArray.load().fromJsonFile(items_array);

    // should pass
    items.forEach(item -> System.out.println(item.write().toJson()));
    items.forEach(item -> System.out.println(item.getId()));

    // fails
    items.stream().map(Item::getId).forEach(System.out::println);
  }
}
