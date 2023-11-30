package org.checkers.server.requests;

import java.io.Serializable;

public record Move(int fromRow, int fromCol, int toRow, int toCol) implements Serializable {
}
