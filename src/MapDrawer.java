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

	int height;
	int width;

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

		setSize(600, 600);
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
		gl.glBegin(GL2.GL_POLYGON);
		gl.glVertex2i(100, 20);
		gl.glVertex2i(100, 460);
		gl.glVertex2i(540, 460);
		gl.glVertex2i(540, 20);
		gl.glEnd();

		DrawArea(gl);
	}

	public void DrawArea(GL2 gl)
	{
		Square sq = new Square(0,0, 500, 500, new float[][] {{2,1},{2,2}});
		sq.DrawContour(1.5f, gl);
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
		gl.glOrtho(0, 640, 0, 480, 0, 10);
		//Handle aspect ratio

		gl.glViewport(x, y,600, 600);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}


	@Override
	public void dispose(GLAutoDrawable glAutoDrawable)
	{
	}


}
