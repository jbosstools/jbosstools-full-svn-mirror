/*
 * ************* DO NOT EDIT THIS FILE ***********
 *
 * This file was automatically generated from IVpeResizer.idl.
 */


/**
 * Interface IVpeResizer
 *
 * IID: 0xC5673CCF-EB08-4cf4-B189-A674C6D3B4A2
 */

public interface IVpeResizer extends nsISupports
{
    public static final String IVPERESIZER_IID_STRING =
        "C5673CCF-EB08-4cf4-B189-A674C6D3B4A2";

    public static final nsID IVPERESIZER_IID =
        new nsID("C5673CCF-EB08-4cf4-B189-A674C6D3B4A2");


    /* const short eTopLeft = 1; */
    public static final short eTopLeft = 1;

    /* const short eTop = 2; */
    public static final short eTop = 2;

    /* const short eTopRight = 4; */
    public static final short eTopRight = 4;

    /* const short eLeft = 8; */
    public static final short eLeft = 8;

    /* const short eRight = 16; */
    public static final short eRight = 16;

    /* const short eBottomLeft = 32; */
    public static final short eBottomLeft = 32;

    /* const short eBottom = 64; */
    public static final short eBottom = 64;

    /* const short eBottomRight = 128; */
    public static final short eBottomRight = 128;

    /* void Init (in nsIDOMDocument aDOMDocument, in nsIPresShell aPresShell); */
    public void init(nsIDOMDocument aDOMDocument, nsIPresShell aPresShell);

    /* void Show (in nsIDOMElement aElement, in PRInt32 resizers); */
    public void show(nsIDOMElement aElement, int resizers);

    /* void Hide (); */
    public void hide();

    /* void AddResizeListener (in IVpeResizeListener aListener); */
    public void addResizeListener(IVpeResizeListener aListener);

    /* void RemoveResizeListener (in IVpeResizeListener aListener); */
    public void removeResizeListener(IVpeResizeListener aListener);

    /* void MouseDown (in PRInt32 aX, in PRInt32 aY, in nsIDOMElement target); */
    public void mouseDown(int aX, int aY, nsIDOMElement target);

    /* void MouseMove (in nsIDOMEvent event); */
    public void mouseMove(nsIDOMEvent event);

    /* void MouseUp (in PRInt32 aX, in PRInt32 aY, in nsIDOMElement target); */
    public void mouseUp(int aX, int aY, nsIDOMElement target);

}

/*
 * end
 */
