import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by alvarpq on 10/5/2016.
 */
public class MapDrawer extends Frame implements GLEventListener
{
	static
	{
		GLProfile.getDefault();
	}

	private GLCanvas canvas;

	GLProfile glProfile = null;
	GLCapabilities glcapabilities = null;
	GLCanvas glcanvas = null;

	double AspectRatio;

	gridFloatReader gfl = new gridFloatReader("ned_86879038");

	int height = 600;
	int width = 1200;

	public MapDrawer()
	{

		super("Mapper");
		glProfile = GLProfile.getDefault();
		glcapabilities = new GLCapabilities(glProfile);

		glcanvas = new GLCanvas(glcapabilities);
		glcanvas.addGLEventListener(this);

		add(glcanvas);

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent windowevent)
			{
				remove(glcanvas);
				dispose();
				System.exit(0);
			}
		});

		setSize(width, height);
		setVisible(true);
	}


	public static void main(String[] args)
	{
		new MapDrawer();
	}

	@Override
	public void init(GLAutoDrawable glAutoDrawable)
	{
		System.out.println("init happening");
		GL2 gl = glAutoDrawable.getGL().getGL2();
		gl.glClearColor(.8f, .8f, .8f, 0f);
	}

	@Override
	public void display(GLAutoDrawable glAutoDrawable)
	{
		System.out.println("Entering display");
		//Get context
		GL2 gl = glAutoDrawable.getGL().getGL2();
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		//Set a color (redish - no other components)
		gl.glColor3f(0.0f, 1f, 0.0f);
		//Define a primitive -  A polygon in this case

		DrawArea(gl, 6, Color.white, Color.black);
	}

	//draws all contour lines
	public void DrawArea(GL2 gl, int stepNum, Color colorStart, Color colorEnd)
	{
		double stepSize = (gfl.maxHeight - gfl.minHeight) / stepNum;
		double[] startColor = new double[] {(double)colorStart.getRed()/255.0, (double)colorStart.getGreen()/255.0,(double)colorStart.getBlue()/255.0};
		double[] endColor = new double[] {(double)colorEnd.getRed()/255.0, (double)colorEnd.getGreen()/255.0,(double)colorEnd.getBlue()/255.0};

		double[] colorStep = new double[3];
		for(int x = 0; x < 3; x++)
		{
			colorStep[x] = (endColor[x] - startColor[x])/stepNum;
		}

		for(int s = 0; s < stepNum; s++)
		{
			DrawLevel((float)gfl.minHeight + s * (float)stepSize, gl, DoubleToColor(Add(startColor, Multiply(s, colorStep))));

		}
	}

	public Color DoubleToColor(double[] color)
	{
		return new Color((float)color[0], (float)color[1], (float)color[2]);
	}

	public double[] Add(double[] v1, double[] v2)
	{
		double[] toRet = new double[v1.length];
		for(int x = 0; x < toRet.length; x++)
		{
			toRet[x] = v1[x] + v2[x];
		}

		return  toRet;
	}

	public double[] Multiply(int scalar, double[] vals)
	{
		double[] toRet = new double[vals.length];
		for(int x = 0; x < vals.length; x++)
		{
			toRet[x] = vals[x] * scalar;
		}

		return  toRet;
	}

	//draws one level of contour lines
	public void DrawLevel(float level, GL2 gl, Color color)
	{
		for(int x = 0; x < gfl.height.length - 1; x++)
		{
			for(int y = 0; y < gfl.height[x].length - 1; y++)
			{
				Square sq = new Square(x,y, 1, 1, new float[][] {{gfl.height[x][y],gfl.height[x][y +1 ]},{gfl.height[x + 1][y],gfl.height[x + 1][y + 1]}});
				sq.DrawContour(level, gl, color);

			}
		}
	}


	@Override
	public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int height, int width)
	{
		System.out.println("Entering reshape(); x=" + x + " y=" + y + " width=" + width + " height=" + height);
		//Get the context
		GL2 gl = glAutoDrawable.getGL().getGL2();
		//Set up projection
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		//this glOrtho call sets up a 640x480 unit plane with a parallel projection.
		gl.glOrtho(0, this.width, 0, this.height, 0, 10);
		//Handle aspect ratio

		gl.glViewport(x, y,this.width, this.height);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}


	@Override
	public void dispose(GLAutoDrawable glAutoDrawable)
	{
	}


}
