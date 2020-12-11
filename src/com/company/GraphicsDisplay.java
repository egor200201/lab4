package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;

import static java.lang.Math.abs;

public class GraphicsDisplay extends JPanel {

    private Font axisFont;

    private Double[][] graphicsData;

    private BasicStroke axisStroke;
    private BasicStroke graphicsStroke;
    private BasicStroke markerStroke;
    private BasicStroke gridStroke;

    private double maxX;
    private double maxY;
    private double minX;
    private double minY;
    private double scale;
    private boolean showAxis = true;
    private boolean showMarkers = true;
    private boolean showGrid = true;

    public GraphicsDisplay() {
        setBackground(Color.WHITE);
        graphicsStroke = new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, new float[]{10,10,10,10,10,10,30,10,20,10,20}, 0.0f);
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        gridStroke = new BasicStroke(0.5f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 5.0f, null, 0.0f);
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);
        axisFont = new Font("Serif", Font.BOLD, 36);
    }

    public void showGraphics(Double[][] graphicsData) {
        this.graphicsData = graphicsData;
        repaint();
    }

    protected Point2D.Double xyToPoint(double x, double y) {
        double deltaX = x - minX;
        double deltaY = maxY - y;
        return new Point2D.Double(deltaX * scale, deltaY * scale);
    }

    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {
        Point2D.Double dest = new Point2D.Double();
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }

    protected void paintGraphics(Graphics2D canvas) {
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.BLUE);
        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i++) {
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {
                graphics.lineTo(point.getX(), point.getY());
            } else {
                graphics.moveTo(point.getX(), point.getY());
            }
        }
        canvas.draw(graphics);
    }

    protected void paintAxis(Graphics2D canvas) {
        canvas.setStroke(axisStroke);
        canvas.setColor(Color.BLACK);
        canvas.setPaint(Color.BLACK);
        canvas.setFont(axisFont);
        FontRenderContext context = canvas.getFontRenderContext();
        if (minX <= 0.0 && maxX >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("y", context);
            Point2D.Double labelPos = xyToPoint(0, maxY);
            canvas.drawString("y", (float) labelPos.getX() + 10,
                    (float) (labelPos.getY() - bounds.getY()));
        }
        Rectangle2D centerBounds = axisFont.getStringBounds("o", context);
        Point2D.Double centerLabelPos = xyToPoint(0, 0);
        canvas.drawString("o", (float)centerLabelPos.getX() + 10,
                (float)(centerLabelPos.getY() - centerBounds.getY()));
        if (minY <= 0.0 && maxY >= 0.0) {
            canvas.draw(new Line2D.Double(xyToPoint(minX, 0),
                    xyToPoint(maxX, 0)));
            GeneralPath arrow = new GeneralPath();
            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());
            arrow.lineTo(arrow.getCurrentPoint().getX() - 20,
                    arrow.getCurrentPoint().getY() - 5);
            arrow.lineTo(arrow.getCurrentPoint().getX(),
                    arrow.getCurrentPoint().getY() + 10);
            arrow.closePath();
            canvas.draw(arrow);
            canvas.fill(arrow);
            Rectangle2D bounds = axisFont.getStringBounds("x", context);
            Point2D.Double labelPos = xyToPoint(maxX, 0);
            canvas.drawString("x",
                    (float) (labelPos.getX() - bounds.getWidth() - 10),
                    (float) (labelPos.getY() + bounds.getY()));
        }
    }

    protected void paintMarkers(Graphics2D canvas) {

        for (Double[] point : graphicsData) {

            boolean temp = false;
            double znach = point[1];
            int qwer = (int)znach;
            if(abs(qwer)%2 == 0) temp = true;

            if (temp) {
                canvas.setColor(Color.RED);
                canvas.setPaint(Color.RED);
            }
            else {
                canvas.setColor(Color.BLUE);
                canvas.setPaint(Color.BLUE);
            }
            canvas.setStroke(markerStroke);
            GeneralPath path = new GeneralPath();
            Point2D.Double center = xyToPoint(point[0], point[1]);
            canvas.draw(new Line2D.Double(shiftPoint(center, 8, 0), shiftPoint(center, -4, 0)));
            canvas.draw(new Line2D.Double(shiftPoint(center, 8, 0), shiftPoint(center, 0, 4)));
            canvas.draw(new Line2D.Double(shiftPoint(center, -8, 0), shiftPoint(center, 0, 4)));
            Point2D.Double corner = shiftPoint(center, 3, 3);

        }
    }

    protected void paintGrid (Graphics2D canvas) {
        double len; int i = 0;
        canvas.setStroke(gridStroke);
        canvas.setColor(Color.BLACK);
        double pos;
        double pos1;
        double pos2;
        double step = (maxX - minX-1)/100;


        pos = (abs(maxY-minY)) *0.5;
        pos1 = (abs(maxY-minY)) *0.9;
        pos2 = (abs(maxY-minY)) *0.1;

            canvas.draw(new Line2D.Double(xyToPoint(minX,pos ), xyToPoint(maxX, pos)));
        canvas.draw(new Line2D.Double(xyToPoint(minX,pos1 ), xyToPoint(maxX, pos1)));
        canvas.draw(new Line2D.Double(xyToPoint(minX,pos2 ), xyToPoint(maxX, pos2)));


    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (graphicsData == null || graphicsData.length == 0) return;
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;
        for (int i = 1; i < graphicsData.length; i++) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
        }
        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);
        scale = Math.min(scaleX, scaleY);
        if (scale == scaleX) {
            double yIncrement = (getSize().getHeight() / scale - (maxY - minY)) / 2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale == scaleY) {
            double xIncrement = (getSize().getWidth() / scale - (maxX - minX)) / 2;
            maxX += xIncrement;
            minX -= xIncrement;
        }
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();
        if (showAxis) paintAxis(canvas);
        paintGraphics(canvas);
        if (showMarkers) paintMarkers(canvas);
        if (showGrid) paintGrid(canvas);
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }


    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }
    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }

}