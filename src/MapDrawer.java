import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by alvarpq on 10/5/2016.
 */
public class MapDrawer extends Frame implements GLEventListener
{
    static {
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

        addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent windowevent ) {
                remove( glcanvas );
                dispose();
                System.exit( 0 );
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
        gl.glClearColor(.8f,.8f,.8f,0f);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable)
    {
        System.out.println("Entering display");
        //Get context
        GL2 gl = glAutoDrawable.getGL().getGL2();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        //Set a color (redish - no other components)
        gl.glColor3f(0.3f,0.0f,0.0f);
        //Define a primitive -  A polygon in this case
        gl.glBegin(GL2.GL_POLYGON);
            gl.glVertex2i( 100, 20);
            gl.glVertex2i( 100,460);
            gl.glVertex2i(540,460);
            gl.glVertex2i(540, 20);
        gl.glEnd();
        DrawLines(Color.yellow, Color.red, 3);
    }

    public void DrawLines(Color low, Color high, int numLines)
    {
        double lowestHeight =  gfl.minHeight;
        double maxHeight = gfl.maxHeight;

        double heigthPerLine = (maxHeight - lowestHeight) / numLines;

        int rIncrement = (high.getRed() - low.getRed()) / numLines;
        int gIncrement = (high.getGreen() - low.getGreen()) / numLines;
        int bIncrement = (high.getBlue() - low.getBlue()) / numLines;

        for(int i = 0; i < numLines; i++)
        {
            Color betweenColor = new Color(low.getRed() + rIncrement * i, low.getBlue() + bIncrement * i, low.getGreen() + gIncrement * i);

            for(int x = 0; x < width - 1; x++)
            {
                for(int y = 0; y < height - 1; y++)
                {
                    SolveSquare(x, y, lowestHeight * i + heigthPerLine, betweenColor);
                }
            }
        }
    }

    public void SolveSquare(int heightX, int heightY, double level, Color color)
    {

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int height, int width)
    {
        System.out.println("Entering reshape(); x="+x+" y="+y+" width="+width+" height="+height);
        //Get the context
        GL2 gl=glAutoDrawable.getGL().getGL2();
        //Set up projection
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        //this glOrtho call sets up a 640x480 unit plane with a parallel projection.
        gl.glOrtho(0,640,0,480,0,10);
        //Handle aspect ratio
        AspectRatio= (float)(this.width)/(float)(this.height);

        if (AspectRatio*height<width)
            gl.glViewport(x, y, (int) (AspectRatio*height), height);
        else
            gl.glViewport(x, y, width, (int) (width/AspectRatio));

        gl.glViewport(x, y, (int) (AspectRatio*height), height);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }


    @Override
    public void dispose(GLAutoDrawable glAutoDrawable)
    {}


}
