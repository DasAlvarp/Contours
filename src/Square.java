/**
 * Created by alvar on 10/18/2016.
 */
public class Square
{
	public  float x;
	public  float y;

	public  float xScale = 1;
	public  float yScale = 1;

	public  float[][] heights = new float[2][2];

	public  Square(float x, float y, float xScale, float yScale, float[][] heights)
	{
		this.x = x;
		this.y = y;
		this.xScale = xScale;
		this.yScale = yScale;
		this.heights = heights;
	}
}
