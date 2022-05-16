package RayCasting;

class Ray {
    private float angle, endX, endY;

    public Ray(float angle, float endX, float endY) {
        this.angle = angle;
        this.endX = endX;
        this.endY = endY;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getEndX() {
        return endX;
    }

    public void setEndX(float endX) {
        this.endX = endX;
    }

    public float getEndY() {
        return endY;
    }

    public void setEndY(float endY) {
        this.endY = endY;
    }

    public boolean sameAs(Ray ray){
        return this.getAngle() == ray.getAngle()
                && this.getEndX() == ray.getEndX()
                && this.getEndY() == ray.getEndY();
    }
}
