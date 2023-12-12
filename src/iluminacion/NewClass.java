package iluminacion;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.GL;
import static com.jogamp.opengl.GL.GL_DEPTH_TEST;
import static com.jogamp.opengl.GL.GL_LEQUAL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import com.jogamp.opengl.awt.GLJPanel;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class Light extends GLJPanel implements GLEventListener, KeyListener {

    private static String TITLE = "Aplicacion de iluminacion";  // window's title
    private static final int CANVAS_WIDTH = 640;  // width of the drawable
    private static final int CANVAS_HEIGHT = 480; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second
    private static final float factInc = 5.0f; // animator's target frames per second
    private float fovy = 45.0f;

    //////////////// Variables /////////////////////////
    // Referencias de rotacion
    float rotX = 90.0f;
    float rotY = 0.0f;
    float rotZ = 0.0f;

    // Posicion de la luz.
    float lightX = 1f;
    float lightY = 1f;
    float lightZ = 1f;
    float dLight = 0.05f;

    // Posicion de la camara
    float camX = 2.0f;
    float camY = 2.0f;
    float camZ = 8.0f;

    // Material y luces.       R        G       B      A
    final float ambient[] = {0.282f, 0.427f, 0.694f, 1.0f};
    final float position[] = {lightX, lightY, lightZ, 1.0f};

    //                                R    G    B    A
    final float[] colorBlack = {0.0f, 0.0f, 0.0f, 1.0f};
    final float[] colorWhite = {1.0f, 1.0f, 1.0f, 1.0f};
    final float[] colorGray = {0.4f, 0.4f, 0.4f, 1.0f};
    final float[] colorDarkGray = {0.2f, 0.2f, 0.2f, 1.0f};
    final float[] colorRed = {1.0f, 0.0f, 0.0f, 1.0f};
    final float[] colorGreen = {0.0f, 1.0f, 0.0f, 1.0f};
    final float[] colorBlue = {0.0f, 0.0f, 0.6f, 1.0f};
    final float[] colorYellow = {1.0f, 1.0f, 0.0f, 1.0f};
    final float[] colorLightYellow = {.5f, .5f, 0.0f, 1.0f};

    //       R     G     B     A          
    final float mat_diffuse[] = {0.6f, 0.6f, 0.6f, 1.0f};
    final float mat_specular[] = {1.0f, 1.0f, 1.0f, 1.0f};
    final float mat_shininess[] = {50.0f};
    private float aspect;

    ///////////////// Funciones /////////////////////////
    public Light() {
        this.addGLEventListener(this);
        this.addKeyListener(this);
    }

    /////////////// Define Luz y Material /////////
    private GLU glu;  // para las herramientas GL (GL Utilities)
    private GLUT glut;

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        // Establece un color de fondo por defecto.
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // set background (clear) color

        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do

        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        // Configuración de luz ambiental global
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, this.ambient, 0);

        // Activar la iluminación y la luz 0.
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);

        // Configuración de la luz 0
        float[] diffuseLight = {0.3f, 0.3f, 0.3f, 1.0f};  // Menos intensa
        float[] specularLight = {0.05f, 0.05f, 0.05f, 1.0f}; // Menos brillo
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, specularLight, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);

        // Configurar la posición inicial de la luz
        this.initPosition(gl);

        // Inicializar las utilidades GLU y GLUT
        glu = new GLU();
        glut = new GLUT();
    }

    public void initPosition(GL2 gl) {
        float posLight1[] = {lightX, lightY, lightZ, 1.0f};
        float spotDirection1[] = {0.0f, -1.f, 0.f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posLight1, 0);
    }

    /////////////// Move light ////////////////////////////
    // Move light 0.
    public void moveLightX(boolean positivDirection) {
        lightX += positivDirection ? dLight : -dLight;
    }

    public void moveLightY(boolean positivDirection) {
        lightY += positivDirection ? dLight : -dLight;
    }

    public void moveLightZ(boolean positivDirection) {
        lightZ += positivDirection ? dLight : -dLight;
    }

    public void animate(GL2 gl, GLU glu, GLUT glut) {
        float posLight0[] = {lightX, lightY, lightZ, 1.f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posLight0, 0);
        drawLight(gl, glu, glut);
        //lightX += 0.003f;
        //lightY += 0.003f;
    }

    /////////////// Define Material /////////////////////
    public void setLightSphereMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorBlue, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorBlue, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, colorBlue, 0);
        gl.glMateriali(face, GL2.GL_SHININESS, 100);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorBlue, 0);
        //gl.glMaterialfv( face, GL.GL_EMISSION, colorLightYellow , 0 );
        //gl.glMaterialfv( face, GL.GL_EMISSION, colorBlack , 0 );
    }

    public void setSomeMaterial(GL2 gl, int face, float rgba[], int offset) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, rgba, offset);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, rgba, offset);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, rgba, offset);
        gl.glMaterialfv(face, GL2.GL_SHININESS, rgba, offset);
        gl.glMateriali(face, GL2.GL_SHININESS, 4);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorBlack, 0);
    }

    public void setSomeMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, mat_diffuse, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, mat_specular, 0);
        gl.glMaterialfv(face, GL2.GL_SHININESS, mat_shininess, 0);
    }

    public void setSomeWhiteMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorWhite, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorWhite, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, colorWhite, 0);
        gl.glMateriali(face, GL2.GL_SHININESS, 4);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorBlack, 0);
    }

    public void setSomeGrayMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorGray, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorGray, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, colorGray, 0);
        gl.glMateriali(face, GL2.GL_SHININESS, 4);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorBlack, 0);
    }

    public void setSomeDarkGrayMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorDarkGray, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorDarkGray, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, colorDarkGray, 0);
        gl.glMateriali(face, GL2.GL_SHININESS, 4);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorBlack, 0);
    }

    public void setSomeYellowMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorBlack, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorLightYellow, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, colorYellow, 0);
        gl.glMateriali(face, GL2.GL_SHININESS, 5);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorBlack, 0);
    }

    public void setSomeBlueMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorBlue, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorBlue, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, colorBlue, 0);
        gl.glMateriali(face, GL2.GL_SHININESS, 4);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorBlack, 0);
    }

    public void setSomeRedMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorRed, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorRed, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, colorWhite, 0);
        gl.glMateriali(face, GL2.GL_SHININESS, 100);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorBlack, 0);
    }

    public void setSomeGreenMaterial(GL2 gl, int face) {
        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorDarkGray, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorGreen, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, colorWhite, 0);
        gl.glMateriali(face, GL2.GL_SHININESS, 128);
        gl.glMaterialfv(face, GL2.GL_EMISSION, colorDarkGray, 0);
    }

    /////////////////// dibujos /////////////////////////
    ///////////////// Dibuja una Esfera con Luz ///////////////
    public void drawLight(GL2 gl, GLU glu, GLUT glut) {
        setLightSphereMaterial(gl, GL.GL_FRONT);
        gl.glPushMatrix();
        {
            gl.glTranslatef(lightX, lightY, lightZ);
            glut.glutSolidSphere(0.1f, 20, 20);
        }
        gl.glPopMatrix();
    }

    public void drawAvocado(GL2 gl, GLUT glut) {
        // Configurar material para el cuerpo del aguacate
        this.setMaterialForAvocado(gl, GL.GL_FRONT);

        // Dibujar el cuerpo del aguacate como un óvalo
        gl.glPushMatrix();
        {
            gl.glTranslatef(2.8f, 0.8f, 2.0f); // Ajustar la posición del cuerpo
            gl.glScalef(0.5f, 0.7f, 0.4f); // Escalar para formar un óvalo
            glut.glutSolidSphere(0.5f, 30, 30); // Esfera base para el cuerpo
        }
        gl.glPopMatrix();


        // Dibujar el cuerpo del aguacate como un óvalo interior
        this.setMaterialForAvocado1(gl, GL.GL_FRONT);
        gl.glPushMatrix();
        {
            gl.glTranslatef(2.8f, 0.8f, 1.9f); // Ajustar la posición del cuerpo
            gl.glScalef(0.5f * 0.75f, 0.77f * 0.75f, 0.3f * 0.75f); // Escalar para formar un óvalo más pequeño
            glut.glutSolidSphere(0.5f, 30, 30); // Esfera base para el cuerpo
        }
        gl.glPopMatrix();


        // Posiciones de las piernas y brazos relativas al cuerpo del aguacate
        float baseY = 0.35f; // Base para la posición Y de las piernas
        float baseX = 2.8f;   // Base para la posición X (centro del cuerpo del aguacate)
        float baseZ = 2.0f;   // Base para la posición Z (centro del cuerpo del aguacate)

        gl.glPushMatrix();
        {
            gl.glTranslatef(baseX - 0.04f, baseY+0.8f, baseZ); // Ajustar la posición de la pierna izquierda
            gl.glRotatef(130.0f, 1.1f, -0.02f, 0.8f); // Rotar para alinear verticalmente
            glut.glutSolidCylinder(0.05f, 0.006f, 30, 30); // Cilindro para la pierna
        }
        gl.glPopMatrix();


        // Configurar material marrón para las piernas y brazos
        this.setBrownMaterial(gl, GL.GL_FRONT);


        float armAngle = 145.0f; // Ángulo para los brazos
        float eyeSize = 0.02f; // Tamaño de los ojos
        float eyeOffsetX = 0.1f; // Desplazamiento horizontal de los ojos
        float eyeOffsetY = 0.62f; // Desplazamiento vertical de los ojos
        float eyeOffsetZ = -0.17f; // Desplazamiento en profundidad de los ojos

        // ombligo
        gl.glPushMatrix();
        {
            gl.glTranslatef(2.8f, 0.7f, 1.8f); // Ajustar la posición del cuerpo
            gl.glScalef(0.6f * 0.30f, 0.6f * 0.30f, 0.1f); // Escalar para formar un óvalo más pequeño
            glut.glutSolidSphere(0.5f, 30, 30); // Esfera base para el cuerpo
        }
        gl.glPopMatrix();

        gl.glPushMatrix();
        {
            gl.glTranslatef(baseX - 0.06f, baseY+0.8f, baseZ); // Ajustar la posición de la pierna izquierda
            gl.glRotatef(-130.0f, 1.1f, -0.02f, 0.5f); // Rotar para alinear verticalmente
            glut.glutSolidCylinder(0.05f, 0.006f, 30, 30); // Cilindro para la pierna
        }
        gl.glPopMatrix();




        // Ojo izquierdo
        gl.glPushMatrix();
        {
            gl.glTranslatef(baseX - eyeOffsetX+0.05f, baseY + eyeOffsetY, baseZ + eyeOffsetZ);
            glut.glutSolidSphere(eyeSize, 20, 20); // Esfera para el ojo izquierdo
        }
        gl.glPopMatrix();

        // Ojo derecho
        gl.glPushMatrix();
        {
            gl.glTranslatef(baseX + eyeOffsetX-0.05f, baseY + eyeOffsetY, baseZ + eyeOffsetZ);
            glut.glutSolidSphere(eyeSize, 20, 20); // Esfera para el ojo derecho
        }
        gl.glPopMatrix();

        // boca
        gl.glPushMatrix();
        {
            gl.glTranslatef(baseX , baseY + 0.59f, baseZ-0.176f); // Ajustar la posición de la pierna izquierda
            gl.glRotatef(-159.0f, 1.0f, 0.0f, 0.0f); // Rotar para alinear verticalmente
            glut.glutSolidCylinder(0.07f, 0.01f, 30, 30); // Cilindro para la pierna
        }
        gl.glPopMatrix();

        // Dibujar las piernas
        // Pierna izquierda
        gl.glPushMatrix();
        {
            gl.glTranslatef(baseX - 0.1f, baseY, baseZ); // Ajustar la posición de la pierna izquierda
            gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f); // Rotar para alinear verticalmente
            glut.glutSolidCylinder(0.05f, 0.3f, 30, 30); // Cilindro para la pierna
        }
        gl.glPopMatrix();

        // Pierna derecha
        gl.glPushMatrix();
        {
            gl.glTranslatef(baseX + 0.1f, baseY, baseZ); // Ajustar la posición de la pierna derecha
            gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f); // Rotar para alinear verticalmente
            glut.glutSolidCylinder(0.05f, 0.3f, 30, 30); // Cilindro para la pierna
        }
        gl.glPopMatrix();

        // Dibujar los brazos
        // Brazo izquierdo
        gl.glPushMatrix();
        {
            gl.glTranslatef(baseX - 0.15f, baseY + 0.55f, baseZ);
            gl.glRotatef(armAngle, 0.0f, 0.0f, 1.0f); // Rotar el brazo
            gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f); // Alinear verticalmente
            glut.glutSolidCylinder(0.05f, 0.4f, 30, 30);
        }
        gl.glPopMatrix();

        // Brazo derecho
        gl.glPushMatrix();
        {
            gl.glTranslatef(baseX + 0.15f, baseY + 0.58f, baseZ);
            gl.glRotatef(-armAngle, 0.0f, 0.0f, 1.0f); // Rotar el brazo
            gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f); // Alinear verticalmente
            glut.glutSolidCylinder(0.05f, 0.4f, 30, 30);
        }
        gl.glPopMatrix();
    }

    // Método para configurar el material de la mesa
    public void setMaterialForAvocado(GL2 gl, int face) {
        float[] colorAvocado = {0.5f, 0.6f, 0.3f, 1.0f}; // Color verde para el aguacate
        float[] specularAvocado = {0.1f, 0.1f, 0.1f, 1.0f}; // Reflectividad baja

        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorAvocado, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorAvocado, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, specularAvocado, 0);
        gl.glMaterialf(face, GL2.GL_SHININESS, 10.0f);
        gl.glMaterialfv(face, GL2.GL_EMISSION, new float[]{0f, 0f, 0f, 1f}, 0);
    }

    public void setMaterialForAvocado1(GL2 gl, int face) {
        float[] colorAvocado = {0.7f, 0.8f, 0.2f, 1.0f}; // Color amarillo verdoso
        float[] specularAvocado = {0.1f, 0.1f, 0.1f, 1.0f}; // Reflectividad baja

        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorAvocado, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorAvocado, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, specularAvocado, 0);
        gl.glMaterialf(face, GL2.GL_SHININESS, 10.0f);
        gl.glMaterialfv(face, GL2.GL_EMISSION, new float[]{0f, 0f, 0f, 1f}, 0);
    }

    public void setBrownMaterial(GL2 gl, int face) {
        float[] colorBrown = {0.607f, 0.478f, 0.404f, 1.0f};// cafe claro
        float[] specularBrown = {0.1f, 0.1f, 0.1f, 1.0f}; // Reflectividad baja

        gl.glMaterialfv(face, GL2.GL_AMBIENT, colorBrown, 0);
        gl.glMaterialfv(face, GL2.GL_DIFFUSE, colorBrown, 0);
        gl.glMaterialfv(face, GL2.GL_SPECULAR, specularBrown, 0);
        gl.glMaterialf(face, GL2.GL_SHININESS, 10.0f);
        gl.glMaterialfv(face, GL2.GL_EMISSION, new float[]{0f, 0f, 0f, 1f}, 0);
    }





    @Override
    public void dispose(GLAutoDrawable glad) {

    }

    @Override
    public void display(GLAutoDrawable glad) {

        GL2 gl = glad.getGL().getGL2();  // get the OpenGL 2 graphics context

        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();
        glu.gluPerspective(fovy, aspect, 0.1, 20.0);
        glu.gluLookAt(this.camX, this.camY, this.camZ, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();  // reset the model-view matrix

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        //this.setSomeGreenMaterial( gl, GL.GL_FRONT );      
        //this.drawFigure(gl, glut);
        gl.glTranslatef(-2.0f, 0.0f, -2.0f);
        this.drawAvocado(gl, glut);

        this.animate(gl, this.glu, this.glut);

        gl.glFlush();

    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        GL2 gl = glad.getGL().getGL2();  // get the OpenGL 2 graphics context

        if (height == 0) {
            height = 1;   // prevent divide by zero
        }
        aspect = (float) width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(fovy, aspect, 0.1, 50.0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset

    }

    public static void main(String[] args) {
        // Run the GUI codes in the event-dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create the OpenGL rendering canvas
                GLJPanel canvas = new Light();
                canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

                // Create animator that drives canvas' display() at the specified FPS.
                final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

                // Create the top-level container
                final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame

                FlowLayout fl = new FlowLayout();
                frame.setLayout(fl);
                
                frame.getContentPane().add(canvas);

                frame.addKeyListener((KeyListener) canvas);

                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        // Use a dedicate thread to run the stop() to ensure that the
                        // animator stops before program exits.
                        new Thread() {
                            @Override
                            public void run() {
                                if (animator.isStarted()) {
                                    animator.stop();
                                }
                                System.exit(0);
                            }
                        }.start();
                    }
                });

                frame.setTitle(TITLE);
                frame.pack();
                frame.setVisible(true);
                animator.start(); // start the animation loop
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int codigo = e.getKeyCode();
        //  lightX, lightY, lightZ
        System.out.println("codigo presionado = " + codigo);

        switch (codigo) {
            case KeyEvent.VK_DOWN:
                this.moveLightY(false);
                break;
            case KeyEvent.VK_UP:
                this.moveLightY(true);
                break;
            case KeyEvent.VK_RIGHT:
                this.moveLightX(true);
                break;
            case KeyEvent.VK_LEFT:
                this.moveLightX(false);
                break;
            case KeyEvent.VK_PAGE_UP:
                this.moveLightZ(false);
                break;
            case KeyEvent.VK_PAGE_DOWN:
                this.moveLightZ(true);
                break;

            case KeyEvent.VK_NUMPAD8:
                this.camY += 0.2f;
                break;
            case KeyEvent.VK_NUMPAD2:
                this.camY -= 0.2f;
                break;
            case KeyEvent.VK_NUMPAD6:
                this.camX += 0.2f;
                break;
            case KeyEvent.VK_NUMPAD4:
                this.camX -= 0.2f;
                break;
            case KeyEvent.VK_Z:
                this.camZ += 0.2f;
                break;
            case KeyEvent.VK_A:
                this.camZ -= 0.2f;
                break;
        }
        System.out.println("rotX = " + rotX);
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void drawOval(GL2 gl, double d, double d0, int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private void setSomeOrangeMaterial(GL2 gl, int GL_FRONT) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}