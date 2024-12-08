package Visual.Classes;

import com.google.gson.*;
import javafx.geometry.Point2D;

import java.lang.reflect.Type;

public class Point2DAdapter implements JsonDeserializer<Point2D>, JsonSerializer<Point2D> {

    @Override
    public Point2D deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        double x = obj.get("x").getAsDouble();
        double y = obj.get("y").getAsDouble();
        return new Point2D(x, y);
    }

    @Override
    public JsonElement serialize(Point2D src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("x", src.getX());
        obj.addProperty("y", src.getY());
        return obj;
    }
}
