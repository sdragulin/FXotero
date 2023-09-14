package org.sk.pdfreader.view;

import javafx.scene.image.Image;
import org.sk.pdfreader.tuples.Pair;

public record PDFPageDTO(int index, float zoom,Image image, Pair<Float,Float> dimension) {
}
