#include "VpeDnD.h"

#include "DragListener.h"

#include "nsIDOMDocument.h"
#include "nsIPresShell.h"
#include "nsIPresContext.h"
#include "nsIView.h"
#include "nsIFrame.h"
#include "nsIDOMHTMLDocument.h"
#include "nsIDOMHTMLElement.h"
#include "nsIDOMNSHTMLElement.h"
#include "nsString.h"
#include "nsIDOMEventReceiver.h"
#include "nsIContent.h"
#include "nsIDOMNSHTMLInputElement.h"
#include "nsIViewManager.h"
#include "nsIEventStateManager.h"
#include "nsIDOMNode.h"

#ifdef _DEBUG
#include <iostream>
using namespace std;
#endif

/* Implementation file */
NS_IMPL_ISUPPORTS1(VpeDnD, IVpeDnD)

VpeDnD::VpeDnD()
:m_DragDropListener(nsnull)
{
  /* member initializers and constructor code */
}

VpeDnD::~VpeDnD()
{
  /* destructor code */
}

NS_IMETHODIMP VpeDnD::Init(nsIDOMDocument *aDOMDocument,
							  nsIPresShell *aPresShell,
							  IVpeDragDropListener *aListener,
							  const nsAString &aPathSeparator)
{
	m_DocWeak = do_GetWeakReference(aDOMDocument);
	m_PresShellWeak = do_GetWeakReference(aPresShell);
	
	nsresult res;
	res = NS_NewDragDropListener(getter_AddRefs(m_DragDropListener), aPresShell, aDOMDocument, aListener, aPathSeparator);
	if (NS_FAILED(res)) {
		return res;
	}

	nsCOMPtr<nsIDOMEventReceiver> erP;
	res = GetDOMEventReceiver(getter_AddRefs(erP));
	if (NS_FAILED(res)) {
		return res;
	}
	
/*
	nsCOMPtr<nsIDOMEventTarget> erP = do_QueryInterface(aDOMDocument, &res);
	if (NS_FAILED(res)) {
		HandleEventListenerError();
		return res;
	}
*/
/*
    res = erP->AddEventListener(NS_LITERAL_STRING("dragdrop"), m_VpeDragListener, PR_TRUE);
	if (NS_FAILED(res)) {
		HandleEventListenerError();
		return res;
	}
    res = erP->AddEventListener(NS_LITERAL_STRING("dragover"), m_VpeDragListener, PR_TRUE);
	if (NS_FAILED(res)) {
		HandleEventListenerError();
		return res;
	}
    res = erP->AddEventListener(NS_LITERAL_STRING("dragexit"), m_VpeDragListener, PR_TRUE);
	if (NS_FAILED(res)) {
		HandleEventListenerError();
		return res;
	}
    res = erP->AddEventListener(NS_LITERAL_STRING("dragenter"), m_VpeDragListener, PR_TRUE);
	if (NS_FAILED(res)) {
		HandleEventListenerError();
		return res;
	}
    res = erP->AddEventListener(NS_LITERAL_STRING("draggesture"), m_VpeDragListener, PR_TRUE);
	if (NS_FAILED(res)) {
		HandleEventListenerError();
		return res;
	}
*/	
	res = erP->AddEventListenerByIID(m_DragDropListener, NS_GET_IID(nsIDOMDragListener));

	return res;
}

NS_IMETHODIMP
VpeDnD::GetBounds(nsIDOMNode *aNode, PRInt32 *aX, PRInt32 *aY, PRInt32 *aWidth, PRInt32 *aHeight)
{
	PRUint16 nodeType;
	aNode->GetNodeType(&nodeType);

	if (!m_PresShellWeak) return NS_ERROR_NOT_INITIALIZED;
	nsCOMPtr<nsIPresShell> ps = do_QueryReferent(m_PresShellWeak);
	if (!ps) return NS_ERROR_NOT_INITIALIZED;
	
	nsCOMPtr<nsIContent> content = do_QueryInterface(aNode);
	nsIFrame *frame = 0; // not ref-counted
	ps->GetPrimaryFrameFor(content, &frame);
	
	float t2p;
	nsCOMPtr<nsIPresContext> pcontext;
	ps->GetPresContext(getter_AddRefs(pcontext));
	pcontext->GetTwipsToPixels(&t2p);

	if (nodeType == nsIDOMNode::ELEMENT_NODE) {
		nsCOMPtr<nsIDOMNSHTMLElement> nsElement = do_QueryInterface(aNode);
		if (nsElement) {
			nsresult res = nsElement->GetOffsetWidth(aWidth);
			if (NS_FAILED(res)) return res;
			
			res = nsElement->GetOffsetHeight(aHeight);
			if (NS_FAILED(res)) return res;
		} else {
			*aWidth = 0;
			*aHeight = 0;
		}
	}

	PRInt32 offsetX = 0, offsetY = 0;
	PRBool firstFrame = PR_TRUE;
	while (frame) {
		// Look for a widget so we can get screen coordinates
		nsIView* view = frame->GetViewExternal();
		if (view && view->HasWidget())
			break;
		
		// No widget yet, so count up the coordinates of the frame 
		if (firstFrame && nodeType != nsIDOMNode::ELEMENT_NODE) {
			nsRect rect = frame->GetRect();
			*aWidth = NSTwipsToIntPixels(rect.width , t2p);
			*aHeight = NSTwipsToIntPixels(rect.height , t2p);
			firstFrame = PR_FALSE;
		}
		nsPoint origin = frame->GetPosition();
		offsetX += origin.x;
		offsetY += origin.y;
		
		frame = frame->GetParent();
	}
	
	*aX = NSTwipsToIntPixels(offsetX , t2p);
	*aY = NSTwipsToIntPixels(offsetY , t2p);

	return NS_OK;
}

NS_IMETHODIMP
VpeDnD::SetCursor(const nsAString & aCursorName, PRInt32 aLock)
{
	nsresult rv = NS_OK;
	PRInt32 cursor;

	// use C strings to keep the code/data size down
	NS_ConvertUCS2toUTF8 cursorString(aCursorName);

	if (cursorString.Equals("auto"))
		cursor = NS_STYLE_CURSOR_AUTO;
	else if (cursorString.Equals("default"))
		cursor = NS_STYLE_CURSOR_DEFAULT;
	else if (cursorString.Equals("pointer"))
		cursor = NS_STYLE_CURSOR_POINTER;
	else if (cursorString.Equals("crosshair"))
		cursor = NS_STYLE_CURSOR_CROSSHAIR;
	else if (cursorString.Equals("move"))
		cursor = NS_STYLE_CURSOR_MOVE;
	else if (cursorString.Equals("text"))
		cursor = NS_STYLE_CURSOR_TEXT;
	else if (cursorString.Equals("wait"))
		cursor = NS_STYLE_CURSOR_WAIT;
	else if (cursorString.Equals("help"))
		cursor = NS_STYLE_CURSOR_HELP;
	else if (cursorString.Equals("n-resize"))
		cursor = NS_STYLE_CURSOR_N_RESIZE;
	else if (cursorString.Equals("s-resize"))
		cursor = NS_STYLE_CURSOR_S_RESIZE;
	else if (cursorString.Equals("w-resize"))
		cursor = NS_STYLE_CURSOR_W_RESIZE;
	else if (cursorString.Equals("e-resize"))
		cursor = NS_STYLE_CURSOR_E_RESIZE;
	else if (cursorString.Equals("ne-resize"))
		cursor = NS_STYLE_CURSOR_NE_RESIZE;
	else if (cursorString.Equals("nw-resize"))
		cursor = NS_STYLE_CURSOR_NW_RESIZE;
	else if (cursorString.Equals("se-resize"))
		cursor = NS_STYLE_CURSOR_SE_RESIZE;
	else if (cursorString.Equals("sw-resize"))
		cursor = NS_STYLE_CURSOR_SW_RESIZE;
	else if (cursorString.Equals("copy"))
		cursor = NS_STYLE_CURSOR_COPY;      // CSS3
	else if (cursorString.Equals("alias"))
		cursor = NS_STYLE_CURSOR_ALIAS;
	else if (cursorString.Equals("context-menu"))
		cursor = NS_STYLE_CURSOR_CONTEXT_MENU;
	else if (cursorString.Equals("cell"))
		cursor = NS_STYLE_CURSOR_CELL;
	else if (cursorString.Equals("grab"))
		cursor = NS_STYLE_CURSOR_GRAB;
	else if (cursorString.Equals("grabbing"))
		cursor = NS_STYLE_CURSOR_GRABBING;
	else if (cursorString.Equals("spinning"))
		cursor = NS_STYLE_CURSOR_SPINNING;
	else if (cursorString.Equals("count-up"))
		cursor = NS_STYLE_CURSOR_COUNT_UP;
	else if (cursorString.Equals("count-down"))
		cursor = NS_STYLE_CURSOR_COUNT_DOWN;
	else if (cursorString.Equals("count-up-down"))
		cursor = NS_STYLE_CURSOR_COUNT_UP_DOWN;
	else if (cursorString.Equals("-moz-zoom-in"))
		cursor = NS_STYLE_CURSOR_MOZ_ZOOM_IN;
	else if (cursorString.Equals("-moz-zoom-out"))
		cursor = NS_STYLE_CURSOR_MOZ_ZOOM_OUT;
	else
		return NS_OK;

	nsCOMPtr<nsIPresShell> ps = do_QueryReferent(m_PresShellWeak);
	if (!ps) return NS_ERROR_NOT_INITIALIZED;
	
	nsCOMPtr<nsIPresContext> presContext;
	ps->GetPresContext(getter_AddRefs(presContext));

	if (presContext) {
		nsCOMPtr<nsIEventStateManager> esm;
		if (NS_SUCCEEDED(presContext->GetEventStateManager(getter_AddRefs(esm)))) {
			// Need root widget.
			nsIViewManager* vm = ps->GetViewManager();
			NS_ENSURE_TRUE(vm, NS_ERROR_FAILURE);

			nsIView *rootView;
			vm->GetRootView(rootView);
			NS_ENSURE_TRUE(rootView, NS_ERROR_FAILURE);

			nsIWidget* widget = rootView->GetWidget();
			NS_ENSURE_TRUE(widget, NS_ERROR_FAILURE);

			// Call esm and set cursor.
			rv = esm->SetCursor(cursor, widget, (aLock == 1 ? PR_TRUE : PR_FALSE) );
		}
	}

	return rv;
}

nsresult 
VpeDnD::GetDOMEventReceiver(nsIDOMEventReceiver **aEventReceiver) 
{ 
	if (!aEventReceiver) 
		return NS_ERROR_NULL_POINTER; 
	
	*aEventReceiver = 0; 
	
	nsCOMPtr<nsIDOMElement> rootElement; 
	
	nsresult result = GetRootElement(getter_AddRefs(rootElement)); 
	
	if (NS_FAILED(result)) 
		return result; 
	
	if (!rootElement) 
		return NS_ERROR_FAILURE; 
	
	// Now hack to make sure we are not anonymous content. 
	// If we are grab the parent of root element for our observer. 
	
	nsCOMPtr<nsIContent> content = do_QueryInterface(rootElement); 
	
	if (content) 
	{ 
		nsIContent* parent = content->GetParent();
		if (parent)
		{ 
			if (parent->IndexOf(content) < 0)
			{ 
				rootElement = do_QueryInterface(parent); //this will put listener on the form element basically 
				result = CallQueryInterface(rootElement, aEventReceiver); 
			} 
			else 
				rootElement = 0; // Let the event receiver work on the document instead of the root element 
		} 
	} 
	else 
		rootElement = 0; 
	
	if (!rootElement && m_DocWeak) 
	{ 
		// Don't use getDocument here, because we have no way of knowing if 
		// Init() was ever called.  So we need to get the document ourselves, 
		// if it exists. 
		
		nsCOMPtr<nsIDOMDocument> domdoc = do_QueryReferent(m_DocWeak); 
		
		if (!domdoc) 
			return NS_ERROR_FAILURE; 
		
		result = domdoc->QueryInterface(NS_GET_IID(nsIDOMEventReceiver), (void **)aEventReceiver); 
	} 
	
	return result; 
} 

nsresult
VpeDnD::GetRootElement(nsIDOMElement **aBodyElement)
{
	if (!aBodyElement)
		return NS_ERROR_NULL_POINTER;
	
	*aBodyElement = 0;
	
	if (m_BodyElement)
	{
		// if we have cached the body element, use that
		*aBodyElement = m_BodyElement;
		NS_ADDREF(*aBodyElement);
		return NS_OK;
	}
	
	nsCOMPtr<nsIDOMHTMLDocument> doc = do_QueryReferent(m_DocWeak);
	if (!doc) return NS_ERROR_NOT_INITIALIZED;
	
	nsCOMPtr<nsIDOMHTMLElement>bodyElement; 
	nsresult result = doc->GetBody(getter_AddRefs(bodyElement));
	if (NS_FAILED(result))
		return result;
	
	if (!bodyElement)
		return NS_ERROR_NULL_POINTER;
	
	// Use the first body node in the list:
	m_BodyElement = do_QueryInterface(bodyElement);
	*aBodyElement = bodyElement;
	NS_ADDREF(*aBodyElement);
	
	return NS_OK;
}






















