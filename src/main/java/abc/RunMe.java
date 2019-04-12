package abc;

import abc.schema.*;
import manifold.api.json.IJsonList;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class RunMe {
  static File single_item, items_array, complex_items_array;

  public static void main(String[] args) throws URISyntaxException {
    ClassLoader classLoader = RunMe.class.getClassLoader();
    single_item = new File(classLoader.getResource("abc.data/single_item.json").toURI());
    items_array = new File(classLoader.getResource("abc.data/items_array.json").toURI());
    complex_items_array = new File(classLoader.getResource("abc.data/complex_items_array.json").toURI());

    // successful pass is a precondition
    loadSingleItem();

    loadItemsArray_PlainInterface();
    loadItemsArray_ExtraMediateType();

    loadComplexItemsArray_objectProperty();
    loadComplexItemsArray_refObjectProperty();
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

  private static void loadComplexItemsArray_objectProperty() {
    List<Item> items = ItemsArray.load().fromJsonFile(complex_items_array);

    // should pass
    items.stream().map(item -> item.getObjProp()).collect(toList());
    items.stream().map(item -> item.getObjProp().getText()).collect(toList());

    // should pass as well
    Map<Integer, Item> itemsMap = items.stream()
            .collect(toMap(item -> item.getId(), identity()));
    Map<String, List<Item>> groupedItemsMap = itemsMap.values().stream()
            .collect(groupingBy(item -> item.getObjProp().getText()));
    groupedItemsMap.entrySet().stream()
            .forEach(e -> System.out.println(e.getKey() + " —> " + e.getValue().stream()
                    .map(objProp -> Objects.toString(objProp.getId()))
                    .collect(Collectors.joining(", "))
            ));
  }

  private static void loadComplexItemsArray_refObjectProperty() {
    List<Item> items = ItemsArray.load().fromJsonFile(complex_items_array);

    // should pass
    items.stream().map(item -> item.getRefObjProp()).collect(toList());
    items.stream().map(item -> item.getRefObjProp().getText()).collect(toList());

    // should pass as well
    Map<Integer, Item> itemsMap = items.stream()
            .collect(toMap(item -> item.getId(), identity()));
    Map<String, List<Item>> groupedItemsMap = itemsMap.values().stream()
            .collect(groupingBy(item -> item.getRefObjProp().getText()));
    groupedItemsMap.entrySet().stream()
            .forEach(e -> System.out.println(e.getKey() + " —> " + e.getValue().stream()
                    .map(objProp -> Objects.toString(objProp.getId()))
                    .collect(Collectors.joining(", "))
            ));
  }
}
