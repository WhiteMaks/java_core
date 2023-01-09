package core.utils;

import core.CoreFactory;
import core.supports.CustomLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OBJFile {
    private final static CustomLogger logger = CoreFactory.getInstance().createLogger(OBJFile.class);

    private final String body;

    private final List<Float> positions;
    private final List<Integer> indices;
    private final List<Face> faces;

    private List<Float> textureCoordinates;
    private List<Float> normals;

    public OBJFile(String body) {
        this.body = body;

        positions = new ArrayList<>();
        textureCoordinates = new ArrayList<>();
        normals = new ArrayList<>();
        indices = new ArrayList<>();
        faces = new ArrayList<>();
    }

    public void parse() {
        for (var line : body.split("\n")) {
            var params = line.split("\\s+");

            if (params.length == 0) {
                continue;
            }

            switch (params[0]) {
                case "v" -> {
                    positions.add(Float.parseFloat(params[1]));
                    positions.add(Float.parseFloat(params[2]));
                    positions.add(Float.parseFloat(params[3]));
                }

                case "vt" -> {
                    textureCoordinates.add(Float.parseFloat(params[1]));
                    textureCoordinates.add(Float.parseFloat(params[2]));
                }

                case "vn" -> {
                    normals.add(Float.parseFloat(params[1]));
                    normals.add(Float.parseFloat(params[2]));
                    normals.add(Float.parseFloat(params[3]));
                }

                case "f" -> faces.add(
                        new Face(
                                params[1],
                                params[2],
                                params[3]
                        )
                );

                default -> {}
            }

        }

        updateArrayData();
    }

    public List<Float> getPositions() {
        return positions;
    }

    public List<Float> getTextureCoordinates() {
        return textureCoordinates;
    }

    public List<Float> getNormals() {
        return normals;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    private void updateArrayData() {
        var tempTextureCoordinates = new float[positions.size() / 3 * 2];
        var tempNormals = new float[positions.size()];

        for (var face : faces) {
            for (var indexGroup : face.indexGroups) {
                var indexPosition = indexGroup.indexPosition;

                indices.add(indexPosition);

                if (indexGroup.indexTextureCoordinate >= 0) {
                    tempTextureCoordinates[indexPosition * 2] = textureCoordinates.get(indexGroup.indexTextureCoordinate * 2);
                    tempTextureCoordinates[indexPosition * 2 + 1] = 1 - textureCoordinates.get(indexGroup.indexTextureCoordinate * 2 + 1);
                }

                if (indexGroup.indexNormal >= 0) {
                    tempNormals[indexPosition * 3] = normals.get(indexGroup.indexNormal * 3);
                    tempNormals[indexPosition * 3 + 1] = normals.get(indexGroup.indexNormal * 3 + 1);
                    tempNormals[indexPosition * 3 + 2] = normals.get(indexGroup.indexNormal * 3 + 2);
                }
            }
        }

        textureCoordinates = new ArrayList<>();
        normals = new ArrayList<>();

        for (float tempTextureCoordinate : tempTextureCoordinates) {
            textureCoordinates.add(tempTextureCoordinate);
        }

        for (float tempNormal : tempNormals) {
            normals.add(tempNormal);
        }
    }

    private static final class Face {
        private final IndexGroup[] indexGroups;

        public Face(String param1, String param2, String param3) {
            indexGroups = new IndexGroup[3];

            indexGroups[0] = parse(param1);
            indexGroups[1] = parse(param2);
            indexGroups[2] = parse(param3);
        }

        private IndexGroup parse(String param) {
            var result = new IndexGroup();

            var values = param.split("/");

            result.indexPosition = Integer.parseInt(values[0]) - 1;

            if (values.length > 1) {
                var textureCoordinateValue = values[1];

                result.indexTextureCoordinate = textureCoordinateValue.length() > 0 ? Integer.parseInt(textureCoordinateValue) - 1 : IndexGroup.NO_VALUE;
            }

            if (values.length > 2) {
                result.indexNormal = Integer.parseInt(values[2]) - 1;
            }

            return result;
        }
    }

    private static final class IndexGroup {
        private static final int NO_VALUE = -1;

        private int indexPosition;
        private int indexTextureCoordinate;
        private int indexNormal;

        public IndexGroup() {
            indexPosition = NO_VALUE;
            indexTextureCoordinate = NO_VALUE;
            indexNormal = NO_VALUE;
        }
    }
}
