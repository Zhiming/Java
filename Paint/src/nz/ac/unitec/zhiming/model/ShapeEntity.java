package nz.ac.unitec.zhiming.model;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

public class ShapeEntity implements Serializable {
	
	private static final long serialVersionUID = -9221794352031396767L;
	
	private String shape;
	private Point startPoint;
	private Point endPoint;
	private Color color;

	public ShapeEntity(String shape, Point startPoint, Point endPoint, Color color){
		this.shape = shape;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.color = color;
	}
	
	public ShapeEntity(){}
	
	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endPoint == null) ? 0 : endPoint.hashCode());
		result = prime * result + ((shape == null) ? 0 : shape.hashCode());
		result = prime * result
				+ ((startPoint == null) ? 0 : startPoint.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShapeEntity other = (ShapeEntity) obj;
		if (endPoint == null) {
			if (other.endPoint != null)
				return false;
		} else if (!endPoint.equals(other.endPoint))
			return false;
		if (shape == null) {
			if (other.shape != null)
				return false;
		} else if (!shape.equals(other.shape))
			return false;
		if (startPoint == null) {
			if (other.startPoint != null)
				return false;
		} else if (!startPoint.equals(other.startPoint))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ShapeEntity [shape=" + shape + ", startPoint=" + startPoint
				+ ", endPoint=" + endPoint + "]";
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

}
