/* Implementation file */

#include "VpeResizer.h"
#include "VpeResizerMouseListener.h"
#include "VpeResizerMouseMotionListener.h"

#include "nsIDOMDocument.h"
#include "nsIDOMNode.h"
#include "nsIDOMElement.h"
#include "nsIDOMEventTarget.h"
#include "nsIPresShell.h"
#include "nsIDOMHTMLDocument.h"
#include "nsIDOMHTMLElement.h"
#include "nsIElementFactory.h"
#include "nsINodeInfo.h"
#include "nsIDocument.h"
#include "nsIServiceManagerUtils.h"
#include "nsIDOMNSHTMLElement.h"
#include "nsIPresContext.h"
#include "nsIView.h"
#include "nsIFrame.h"
#include "nsIDOMElementCSSInlineStyle.h"
#include "nsIDOMCSSStyleDeclaration.h"
#include "nsIDOMMouseEvent.h"

#include "nsIViewManager.h"

#ifdef _DEBUG
#include <iostream>

using namespace std;
#endif

#define	MAX_SIZE				20000000

NS_IMPL_ISUPPORTS1(VpeResizer, IVpeResizer)

VpeResizer::VpeResizer()
:m_DocWeak(nsnull)
,m_PresShellWeak(nsnull)
,m_ResizingShadow(nsnull)
,m_BodyElement(nsnull)
,m_usedHandle(0)
,m_ActiveHandle(nsnull)
,m_TopLeftHandle(nsnull)
,m_TopHandle(nsnull)
,m_TopRightHandle(nsnull)
,m_LeftHandle(nsnull)
,m_RightHandle(nsnull)
,m_BottomLeftHandle(nsnull)
,m_BottomHandle(nsnull)
,m_BottomRightHandle(nsnull)
,m_Resizing(PR_FALSE)
{
  /* member initializers and constructor code */
}

VpeResizer::~VpeResizer()
{
  /* destructor code */
}

NS_IMETHODIMP
VpeResizer::Init(nsIDOMDocument *aDOMDocument,
					  nsIPresShell *aPresShell)
{
	m_DocWeak = do_GetWeakReference(aDOMDocument);
	m_PresShellWeak = do_GetWeakReference(aPresShell);

	nsresult res = NS_NewVpeResizerMouseListener(getter_AddRefs(m_MouseListener), this);
	if (NS_FAILED(res)) {
		return res;
	}
	
	return res;
}

NS_IMETHODIMP
VpeResizer::Show(nsIDOMElement *aElement, PRInt32 resizers)
{
	m_ResizingObject = aElement;

	nsresult res = GetPositionAndDimensions(aElement, m_ResizedObjectX, m_ResizedObjectY,
									m_ResizedObjectWidth, m_ResizedObjectHeight,
									m_ResizedObjectBorderLeft, m_ResizedObjectBorderTop,
									m_ResizedObjectMarginLeft, m_ResizedObjectMarginTop);

#ifdef _DEBUG
	cout << "C:VpeResizer::Show|" <<
		" X = " << m_ResizedObjectX <<
		"; Y = " << m_ResizedObjectY <<
		"; Width = " << m_ResizedObjectWidth <<
		"; Height = " << m_ResizedObjectHeight << endl;
#endif

	if (NS_FAILED(res)) return res;
	if ((m_ResizedObjectWidth <= 0) || (m_ResizedObjectWidth > MAX_SIZE) || (m_ResizedObjectHeight <= 0) || (m_ResizedObjectHeight > MAX_SIZE)) return NS_OK;

	nsCOMPtr<nsIDOMElement> bodyElement;

	res = GetRootElement(getter_AddRefs(bodyElement));
	if (NS_FAILED(res)) return res;
	if (!bodyElement)   return NS_ERROR_NULL_POINTER;

	if ((resizers & eTopLeft) == eTopLeft) {
		res = CreateResizer(getter_AddRefs(m_TopLeftHandle), kTopLeft, bodyElement);
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if ((resizers & eTop) == eTop) {
		res = CreateResizer(getter_AddRefs(m_TopHandle), kTop, bodyElement);
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if ((resizers & eTopRight) == eTopRight) {
		res = CreateResizer(getter_AddRefs(m_TopRightHandle), kTopRight, bodyElement);
		if (NS_FAILED(res)) {
			return res;
		}
	}
	
	if ((resizers & eLeft) == eLeft) {
		res = CreateResizer(getter_AddRefs(m_LeftHandle), kLeft, bodyElement);
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if ((resizers & eRight) == eRight) {
		res = CreateResizer(getter_AddRefs(m_RightHandle), kRight, bodyElement);
		if (NS_FAILED(res)) {
			return res;
		}
	}
	
	if ((resizers & eBottomLeft) == eBottomLeft) {
		res = CreateResizer(getter_AddRefs(m_BottomLeftHandle), kBottomLeft, bodyElement);
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if ((resizers & eBottom) == eBottom) {
		res = CreateResizer(getter_AddRefs(m_BottomHandle), kBottom, bodyElement);
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if ((resizers & eBottomRight) == eBottomRight) {
		res = CreateResizer(getter_AddRefs(m_BottomRightHandle), kBottomRight, bodyElement);
		if (NS_FAILED(res)) {
			return res;
		}
	}

	res = SetAllResizersPosition();
	if (NS_FAILED(res)) {
		return res;
	}

	// now, let's create the resizing shadow
	res = CreateShadow(getter_AddRefs(m_ResizingShadow), bodyElement,
			m_ResizingObject);
	if (NS_FAILED(res)) {
		return res;
	}

	res = SetShadowPosition(m_ResizingShadow, m_ResizingObject,
		m_ResizedObjectX, m_ResizedObjectY);
	if (NS_FAILED(res)) {
		return res;
	}
	
	// and then the resizing info tooltip
//	res = CreateResizingInfo(getter_AddRefs(m_ResizingInfo), bodyElement);
//	if (NS_FAILED(res)) {
//		return res;
//	}
	
// Edward
//	m_ResizingObject->SetAttribute(NS_LITERAL_STRING("_moz_resizing"), NS_LITERAL_STRING("true"));

	return NS_OK;
}

NS_IMETHODIMP
VpeResizer::Hide()
{
	nsCOMPtr<nsIDOMElement> bodyElement;
	
	nsresult res = GetRootElement(getter_AddRefs(bodyElement));
	if (NS_FAILED(res)) {
		return res;
	}
	if (!bodyElement) {
		return NS_ERROR_NULL_POINTER;
	}

	nsCOMPtr<nsIDOMNode> parentNode = do_QueryInterface(bodyElement);
	if (!parentNode) {
		return NS_ERROR_NULL_POINTER;
	}

	nsCOMPtr<nsIDOMNode> oldNode;

	if (m_TopLeftHandle) {
		res = parentNode->RemoveChild(m_TopLeftHandle, getter_AddRefs(oldNode));
		oldNode = nsnull;
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if (m_TopHandle) {
		res = parentNode->RemoveChild(m_TopHandle, getter_AddRefs(oldNode));
		oldNode = nsnull;
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if (m_TopRightHandle) {
		res = parentNode->RemoveChild(m_TopRightHandle, getter_AddRefs(oldNode));
		oldNode = nsnull;
		if (NS_FAILED(res)) {
			return res;
		}
	}
	

	if (m_LeftHandle) {
		parentNode->RemoveChild(m_LeftHandle, getter_AddRefs(oldNode));
		oldNode = nsnull;
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if (m_RightHandle) {
		parentNode->RemoveChild(m_RightHandle, getter_AddRefs(oldNode));
		oldNode = nsnull;	
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if (m_BottomLeftHandle) {
		parentNode->RemoveChild(m_BottomLeftHandle, getter_AddRefs(oldNode));
		oldNode = nsnull;
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if (m_BottomHandle) {
		parentNode->RemoveChild(m_BottomHandle, getter_AddRefs(oldNode));
		oldNode = nsnull;
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if (m_BottomRightHandle) {
		parentNode->RemoveChild(m_BottomRightHandle, getter_AddRefs(oldNode));
		oldNode = nsnull;
		if (NS_FAILED(res)) {
			return res;
		}
	}

	parentNode->RemoveChild(m_ResizingShadow, getter_AddRefs(oldNode));
	oldNode = nsnull;
	if (NS_FAILED(res)) {
		return res;
	}
	
	m_TopLeftHandle = nsnull;
	m_TopHandle = nsnull;
	m_TopRightHandle = nsnull;
	m_LeftHandle = nsnull;
	m_RightHandle = nsnull;
	m_BottomLeftHandle = nsnull;
	m_BottomHandle = nsnull;
	m_BottomRightHandle = nsnull;
	
	m_ResizingShadow = nsnull;
/*
#ifdef _DEBUG
	cout << "Hide remove top" << endl;
#endif
	res = RemoveResizerAndShadow(getter_AddRefs(m_TopHandle), bodyElement);
	if (NS_FAILED(res)) {
		return res;
	}
#ifdef _DEBUG
	cout << "Hide remove left" << endl;
#endif
	res = RemoveResizerAndShadow(getter_AddRefs(m_LeftHandle), bodyElement);
	if (NS_FAILED(res)) {
		return res;
	}

#ifdef _DEBUG
	cout << "Hide remove right" << endl;
#endif
	res = RemoveResizerAndShadow(getter_AddRefs(m_RightHandle), bodyElement);
	if (NS_FAILED(res)) {
		return res;
	}

#ifdef _DEBUG
	cout << "Hide remove bottom" << endl;
#endif
	res = RemoveResizerAndShadow(getter_AddRefs(m_BottomHandle), bodyElement);
	if (NS_FAILED(res)) {
		return res;
	}
	
	m_TopHandle = nsnull;
	m_LeftHandle = nsnull;
	m_RightHandle = nsnull;
	m_BottomHandle = nsnull;
	
#ifdef _DEBUG
	cout << "Hide Remove Shadow" << endl;
#endif
	res = RemoveResizerAndShadow(getter_AddRefs(m_ResizingShadow), bodyElement);
	if (NS_FAILED(res)) {
		return res;
	}
	m_ResizingShadow = nsnull;
*/	
	
// Edward
//	m_ResizingObject->RemoveAttribute(NS_LITERAL_STRING("_moz_resizing"));
	m_ResizingObject = nsnull;
	
	return NS_OK;
}

NS_IMETHODIMP
VpeResizer::AddResizeListener(IVpeResizeListener * aListener)
{
	if (objectResizeEventListeners.Count() &&
		objectResizeEventListeners.IndexOf(aListener) != -1) {
		return NS_OK;
	}
	objectResizeEventListeners.AppendObject(aListener);

	return NS_OK;
}

NS_IMETHODIMP
VpeResizer::RemoveResizeListener(IVpeResizeListener * aListener)
{
	NS_ENSURE_ARG_POINTER(aListener);
	if (!objectResizeEventListeners.Count() ||
		objectResizeEventListeners.IndexOf(aListener) == -1) {
		return NS_OK;
	}
	objectResizeEventListeners.RemoveObject(aListener);
	return NS_OK;
}

NS_IMETHODIMP
VpeResizer::MouseDown(PRInt32 aClientX, PRInt32 aClientY, nsIDOMElement *aTarget)
{
	PRBool isAnonElement = PR_FALSE;
	if (aTarget && NS_SUCCEEDED(aTarget->HasAttribute(NS_LITERAL_STRING("_moz_anonclass"), &isAnonElement))) {
		if (isAnonElement) {
			nsAutoString anonclass;
			nsresult res = aTarget->GetAttribute(NS_LITERAL_STRING("_moz_anonclass"), anonclass);
			if (NS_FAILED(res)) return res;
			if (anonclass.Equals(NS_LITERAL_STRING("mozResizer"))) {
				// and that element is a resizer, let's start resizing!
#ifdef _DEBUG
				cout << "C:VpeResizer::MouseDown |Start resizing resizer" << endl 
					<< "aClientX is " << aClientX << endl
					<< "aClientY is " << aClientY << endl;
#endif
				m_OriginalX = aClientX;
				m_OriginalY = aClientY;
				return StartResizing(aTarget);
			}
		}
	}

	return NS_OK;
}

NS_IMETHODIMP
VpeResizer::MouseMove(nsIDOMEvent *event)
{
	if (m_Resizing) {
		NS_NAMED_LITERAL_STRING(leftStr, "left");
		NS_NAMED_LITERAL_STRING(topStr, "top");
		
		nsCOMPtr<nsIDOMMouseEvent> mouseEvent ( do_QueryInterface(event) );
		PRInt32 clientX, clientY;
		mouseEvent->GetClientX(&clientX);
		mouseEvent->GetClientY(&clientY);

		PRInt32 newX = GetNewResizingX(clientX, clientY);
		PRInt32 newY = GetNewResizingY(clientX, clientY);
		PRInt32 newWidth  = GetNewResizingWidth(clientX, clientY);
		PRInt32 newHeight = GetNewResizingHeight(clientX, clientY);


		nsresult res = SetStylePropertyPixels(m_ResizingShadow, leftStr, newX);
		if (NS_FAILED(res)) {
			return res;
		}

		res = SetStylePropertyPixels(m_ResizingShadow, topStr, newY);
		if (NS_FAILED(res)) {
			return res;
		}

		res = SetStylePropertyPixels(m_ResizingShadow, NS_LITERAL_STRING("width"), newWidth);
		if (NS_FAILED(res)) {
			return res;
		}

		res = SetStylePropertyPixels(m_ResizingShadow, NS_LITERAL_STRING("height"), newHeight);
		if (NS_FAILED(res)) {
			return res;
		}
		
#ifdef _DEBUG
		cout << "C:VpeResizer::MouseMove| clientX=" << clientX << " clientY=" << clientY << " NewHeight="<< newHeight << " NewWidth=" << newWidth << endl;
#endif
		
	}

	return NS_OK;
}

NS_IMETHODIMP
VpeResizer::MouseUp(PRInt32 aX, PRInt32 aY, nsIDOMElement *aTarget)
{
	if (m_Resizing) {
		m_Resizing = PR_FALSE;
		
		HideShadowAndInfo();
		EndResizing(aX, aY);
			
//		SetFinalSize(aClientX, aClientY);
		
#ifdef _DEBUG
		cout << "C:VpeResizer::MouseUp| Stop resizing resizer" << endl;
#endif

		nsCOMPtr<nsIDOMEventReceiver> erP;
		nsresult res = GetDOMEventReceiver(getter_AddRefs(erP));
		if (NS_SUCCEEDED(res) && erP) {
			res = erP->RemoveEventListener(NS_LITERAL_STRING("mousemove"), m_MouseMotionListener, PR_TRUE);
			if (NS_FAILED(res)) {
				HandleEventListenerError();
				return res;
			}
			
			res = erP->RemoveEventListener(NS_LITERAL_STRING("mouseup"), m_MouseListener, PR_TRUE);
			if (NS_FAILED(res)) {
				HandleEventListenerError();
				return res;
			}
		}

		m_MouseMotionListener = nsnull;
	}
	
	return NS_OK;
}


nsresult
VpeResizer::GetRootElement(nsIDOMElement **aBodyElement)
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
	
	NS_PRECONDITION(m_DocWeak, "bad state, null mDocWeak");
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


nsresult
VpeResizer::CreateResizer(nsIDOMElement ** aNewResizer, const nsAString &aLocation, nsIDOMNode * aParentNode)
{
	nsresult res = CreateAnonymousElement(NS_LITERAL_STRING("span"),
		aParentNode, NS_LITERAL_STRING("mozResizer"), PR_FALSE, aNewResizer);

	nsCOMPtr<nsIDOMEventTarget> evtTarget(do_QueryInterface(*aNewResizer));
	evtTarget->AddEventListener(NS_LITERAL_STRING("mousedown"), m_MouseListener, PR_TRUE);

	res = (*aNewResizer)->SetAttribute(NS_LITERAL_STRING("anonlocation"), aLocation);

	return res;
}

nsresult
VpeResizer::CreateAnonymousElement(const nsAString & aTag,
								   nsIDOMNode *  aParentNode,
								   const nsAString & aAnonClass,
								   PRBool aIsCreatedHidden,
								   nsIDOMElement ** aNewElement)
{
	nsCOMPtr<nsIDOMDocument> doc = do_QueryReferent(m_DocWeak);
	if (!doc) {
		return NS_ERROR_NO_INTERFACE;
	}
	
	nsresult res = doc->CreateElement(aTag,aNewElement);
	if (NS_FAILED(res))
		return res;
	if (!aNewElement)
		return NS_ERROR_NULL_POINTER;

	// add the "hidden" class if needed
	if (aIsCreatedHidden) {
		res = (*aNewElement)->SetAttribute(NS_LITERAL_STRING("class"),
			NS_LITERAL_STRING("hidden"));
		if (NS_FAILED(res)) {
			return res;
		}
	}
	
	// add an _moz_anonclass attribute if needed
	if (!aAnonClass.IsEmpty()) {
		res = (*aNewElement)->SetAttribute(NS_LITERAL_STRING("_moz_anonclass"),
		  aAnonClass);
		if (NS_FAILED(res)) {
			return res;
		}
	}
	
	nsCOMPtr<nsIDOMNode> node = do_QueryInterface(*aNewElement);
	if (!node) {
		return NS_ERROR_NO_INTERFACE;
	}
	
	nsCOMPtr<nsIDOMNode> child;
	res = aParentNode->AppendChild(node, getter_AddRefs(child));
	if (NS_FAILED(res)) {
		return res;
	}
	
	return NS_OK;
}

nsresult
VpeResizer::CreateHTMLContent(const nsAString& aTag, nsIContent** aContent)
{
	nsresult res;
	
	nsCOMPtr<nsIElementFactory> elementFactory = 
		do_GetService(NS_ELEMENT_FACTORY_CONTRACTID_PREFIX"http://www.w3.org/1999/xhtml", &res);
	if (!elementFactory)
		return NS_ERROR_FAILURE;
	
	nsCOMPtr<nsIDOMDocument> domDoc = do_QueryReferent(m_DocWeak);
	if (NS_FAILED(res)) return res;
	if (!domDoc) return NS_ERROR_FAILURE;
	
	nsCOMPtr<nsIDocument> doc = do_QueryInterface(domDoc);
	
	nsINodeInfoManager *nodeInfoManager = doc->GetNodeInfoManager();
	NS_ENSURE_TRUE(nodeInfoManager, NS_ERROR_FAILURE);
	
	nsCOMPtr<nsINodeInfo> nodeInfo;
	res = nodeInfoManager->GetNodeInfo(aTag, nsnull, kNameSpaceID_None,
		getter_AddRefs(nodeInfo));
	
	if (NS_FAILED(res))
		return res;
	if (!nodeInfo)
		return NS_ERROR_FAILURE;
	
	res = elementFactory->CreateInstanceByTag(nodeInfo, aContent);
	
	if (NS_FAILED(res))
		return res;
	if (!aContent)
		return NS_ERROR_FAILURE;
	
	return NS_OK;
}

nsresult
VpeResizer::GetPositionAndDimensions(nsIDOMElement * aElement,
									 PRInt32 & aX, PRInt32 & aY,
									 PRInt32 & aW, PRInt32 & aH,
									 PRInt32 & aBorderLeft,
									 PRInt32 & aBorderTop,
									 PRInt32 & aMarginLeft,
									 PRInt32 & aMarginTop)
{
	NS_ENSURE_ARG_POINTER(aElement);
/*	
	// Is the element positioned ? let's check the cheap way first...
	PRBool isPositioned = PR_FALSE;
	nsresult res = aElement->HasAttribute(NS_LITERAL_STRING("_moz_abspos"), &isPositioned);
	if (NS_FAILED(res)) return res;
	if (!isPositioned) {
		// hmmm... the expensive way now...
		nsAutoString positionStr;
		mHTMLCSSUtils->GetComputedProperty(aElement, nsEditProperty::cssPosition,
			positionStr);
		isPositioned = positionStr.Equals(NS_LITERAL_STRING("absolute"));
	}
	
	if (isPositioned) {
		// Yes, it is absolutely positioned
		mResizedObjectIsAbsolutelyPositioned = PR_TRUE;
		
		nsCOMPtr<nsIDOMViewCSS> viewCSS;
		res = mHTMLCSSUtils->GetDefaultViewCSS(aElement, getter_AddRefs(viewCSS));
		if (NS_FAILED(res)) return res;
		
		nsAutoString empty;
		nsCOMPtr<nsIDOMCSSStyleDeclaration> cssDecl;
		// Get the all the computed css styles attached to the element node
		res = viewCSS->GetComputedStyle(aElement, empty, getter_AddRefs(cssDecl));
		if (NS_FAILED(res)) return res;
		
		aBorderLeft = GetCSSFloatValue(cssDecl, NS_LITERAL_STRING("border-left-width"));
		aBorderTop  = GetCSSFloatValue(cssDecl, NS_LITERAL_STRING("border-top-width"));
		aMarginLeft = GetCSSFloatValue(cssDecl, NS_LITERAL_STRING("margin-left"));
		aMarginTop  = GetCSSFloatValue(cssDecl, NS_LITERAL_STRING("margin-top"));
		
		aX = GetCSSFloatValue(cssDecl, NS_LITERAL_STRING("left")) +
			aMarginLeft + aBorderLeft;
		aY = GetCSSFloatValue(cssDecl, NS_LITERAL_STRING("top")) +
			aMarginTop + aBorderTop;
		aW = GetCSSFloatValue(cssDecl, NS_LITERAL_STRING("width"));
		aH = GetCSSFloatValue(cssDecl, NS_LITERAL_STRING("height"));
	}
	else {
		mResizedObjectIsAbsolutelyPositioned = PR_FALSE;
*/		nsCOMPtr<nsIDOMNSHTMLElement> nsElement = do_QueryInterface(aElement);
		if (!nsElement) {return NS_ERROR_NULL_POINTER; }
		
		nsresult res = nsElement->GetOffsetWidth(&aW);
		if (NS_FAILED(res)) return res;
		res = nsElement->GetOffsetHeight(&aH);
		if (NS_FAILED(res)) return res;
		
		GetElementOrigin(aElement, aX, aY);
		
		aBorderLeft = 0;
		aBorderTop  = 0;
		aMarginLeft = 0;
		aMarginTop = 0;
//	}
	return res;
}

nsresult
VpeResizer::GetElementOrigin(nsIDOMElement * aElement, PRInt32 & aX, PRInt32 & aY)
{
	// we are going to need the PresShell
	if (!m_PresShellWeak) return NS_ERROR_NOT_INITIALIZED;
	nsCOMPtr<nsIPresShell> ps = do_QueryReferent(m_PresShellWeak);
	if (!ps) return NS_ERROR_NOT_INITIALIZED;
	
	nsCOMPtr<nsIContent> content = do_QueryInterface(aElement);
	nsIFrame *frame = nsnull; // not ref-counted
	ps->GetPrimaryFrameFor(content, &frame);
	
	float t2p;
	nsCOMPtr<nsIPresContext> pcontext;
	ps->GetPresContext(getter_AddRefs(pcontext));
	pcontext->GetTwipsToPixels(&t2p);
	
#if 0
	/* this code is copied from nsIFlasher for calculating 
	 * position like one
	 */
	// inserted
	nsPoint result(0,0);
	nsIView* view;
	frame->GetOffsetFromView(pcontext, result, &view);
	nsIView* rootView = nsnull;
	if (view) {
		nsIViewManager* viewManager = view->GetViewManager();
		NS_ASSERTION(viewManager, "View must have a viewmanager");
		viewManager->GetRootView(rootView);
	}
	while (view) {
		result += view->GetPosition();
		if (view == rootView) {
			break;
		}
		view = view->GetParent();
	}

	aX = NSTwipsToIntPixels(result.x , t2p);
	aY = NSTwipsToIntPixels(result.y , t2p);

	return NS_OK;	
	// end of inserted
#endif

//	if (nsHTMLEditUtils::IsHR(aElement)) {
//		frame = frame->GetNextSibling();
//	}
	PRInt32 offsetX = 0, offsetY = 0;
	while (frame) {
		// Look for a widget so we can get screen coordinates
		nsIView* view = frame->GetViewExternal();
		if (view && view->HasWidget())
			break;
		
		// No widget yet, so count up the coordinates of the frame 
		nsPoint origin = frame->GetPosition();
		offsetX += origin.x;
		offsetY += origin.y;
		
		frame = frame->GetParent();
	}
	
	aX = NSTwipsToIntPixels(offsetX , t2p);
	aY = NSTwipsToIntPixels(offsetY , t2p);
	
	return NS_OK;
}

nsresult
VpeResizer::SetAllResizersPosition()
{
	PRInt32 x = m_ResizedObjectX;
	PRInt32 y = m_ResizedObjectY;
	PRInt32 w = m_ResizedObjectWidth;
	PRInt32 h = m_ResizedObjectHeight;
	
	// now let's place all the resizers around the image
	
	// get the size of resizers
//	float resizerWidth = 5.0, resizerHeight = 5.0;
	PRInt32 resizerWidth = 5, resizerHeight = 5;
//	nsCOMPtr<nsIAtom> dummyUnit;
//	mHTMLCSSUtils->GetComputedProperty(mTopLeftHandle, nsEditProperty::cssWidth, value);
//	mHTMLCSSUtils->ParseLength(value, &resizerWidth, getter_AddRefs(dummyUnit));
//	mHTMLCSSUtils->GetComputedProperty(mTopLeftHandle, nsEditProperty::cssHeight, value);
//	mHTMLCSSUtils->ParseLength(value, &resizerHeight, getter_AddRefs(dummyUnit));
	
	PRInt32 rw = (PRInt32)((resizerWidth + 1) / 2);
	PRInt32 rh = (PRInt32)((resizerHeight+ 1) / 2);
	
	nsresult res = NS_OK;
	if (m_TopLeftHandle) {
		//res = SetAnonymousElementPosition(x-rw,     y-rh, m_TopLeftHandle);
		res = SetAnonymousElementPosition(x-resizerWidth-2, y-resizerHeight-2, m_TopLeftHandle);
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if (m_TopHandle) {
		//res = SetAnonymousElementPosition(x+w/2-rw, y-rh, m_TopHandle);
		res = SetAnonymousElementPosition(x+w/2-rw, y-resizerHeight-2, m_TopHandle);
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if (m_TopRightHandle) {
		//res = SetAnonymousElementPosition(x+w-rw-1, y-rh, m_TopRightHandle);
		res = SetAnonymousElementPosition(x+w, y-resizerHeight-2, m_TopRightHandle);
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if (m_LeftHandle) {
		//res = SetAnonymousElementPosition(x-rw, y+h/2-rh, m_LeftHandle);
		res = SetAnonymousElementPosition(x-resizerWidth-2, y+h/2-rh, m_LeftHandle);
		if (NS_FAILED(res)) {
			return res;
		}
	}
	
	if (m_RightHandle) {
		//res = SetAnonymousElementPosition(x+w-rw-1, y+h/2-rh, m_RightHandle);
		res = SetAnonymousElementPosition(x+w, y+h/2-rh, m_RightHandle);
		if (NS_FAILED(res)) {
			return res;
		}
	}
	
	if (m_BottomLeftHandle) {
		//res = SetAnonymousElementPosition(x-rw,     y+h-rh-1, m_BottomLeftHandle);
		res = SetAnonymousElementPosition(x-resizerWidth-2, y+h, m_BottomLeftHandle);
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if (m_BottomHandle) {
		//res = SetAnonymousElementPosition(x+w/2-rw, y+h-rh-1, m_BottomHandle);
		res = SetAnonymousElementPosition(x+w/2-rw, y+h, m_BottomHandle);
		if (NS_FAILED(res)) {
			return res;
		}
	}

	if (m_BottomRightHandle) {
		//res = SetAnonymousElementPosition(x+w-rw-1, y+h-rh-1, m_BottomRightHandle);
		res = SetAnonymousElementPosition(x+w, y+h, m_BottomRightHandle);
		if (NS_FAILED(res)) {
			return res;
		}
	}
	
	return NS_OK;
}

nsresult
VpeResizer::SetStyle(nsIDOMElement *aElement,const nsAString &aPropertyName,const nsAString &aValue) {
	nsCOMPtr<nsIDOMElementCSSInlineStyle> inlineStyles = do_QueryInterface(aElement);
    if (!inlineStyles) return NS_ERROR_NULL_POINTER;
    nsCOMPtr<nsIDOMCSSStyleDeclaration> cssDecl;
    nsresult res = inlineStyles->GetStyle(getter_AddRefs(cssDecl));
    if (NS_FAILED(res)) return res;
    if (!cssDecl) return NS_ERROR_NULL_POINTER;
	
    if (aValue.IsEmpty()) {
		// an empty value means we have to remove the property
		nsAutoString returnString;
		res = cssDecl->RemoveProperty(aPropertyName, returnString);
    }
    else {
		// let's recreate the declaration as it was
		nsAutoString priority;
		res = cssDecl->GetPropertyPriority(aPropertyName, priority);
		if (NS_FAILED(res)) return res;
		res = cssDecl->SetProperty(aPropertyName, aValue, priority);
    }

	return res;
}

nsresult
VpeResizer::StartResizing(nsIDOMElement *aTarget)
{
	m_Resizing = PR_TRUE;
	
	m_ActiveHandle = aTarget;
	m_ActiveHandle->SetAttribute(NS_LITERAL_STRING("_moz_activated"), NS_LITERAL_STRING("true"));


	nsAutoString locationStr;
	m_usedHandle = 0;
	m_ActiveHandle->GetAttribute(NS_LITERAL_STRING("anonlocation"), locationStr);
	if (locationStr.Equals(kTopLeft)) {
		m_usedHandle = eTopLeft;
		SetResizeIncrements(1, 1, -1, -1, PR_FALSE);
		SetInfoIncrements(20, 20);
	}
	else if (locationStr.Equals(kTop)) {
		m_usedHandle = eTop;
		SetResizeIncrements(0, 1, 0, -1, PR_FALSE);
		SetInfoIncrements(0, 20);
	}
	else if (locationStr.Equals(kTopRight)) {
		m_usedHandle = eTopRight;
		SetResizeIncrements(0, 1, 1, -1, PR_FALSE);
		SetInfoIncrements(-20, 20);
	}
	else if (locationStr.Equals(kLeft)) {
		m_usedHandle = eLeft;
		SetResizeIncrements(1, 0, -1, 0, PR_FALSE);
		SetInfoIncrements(20, 20);
	}
	else if (locationStr.Equals(kRight)) {
		m_usedHandle = eRight;
		SetResizeIncrements(0, 0, 1, 0, PR_FALSE);
		SetInfoIncrements(-20, 0);
	}
	else if (locationStr.Equals(kBottomLeft)) {
		m_usedHandle = eBottomLeft;
		SetResizeIncrements(1, 0, -1, 1, PR_FALSE);
		SetInfoIncrements(20, -20);
	}
	else if (locationStr.Equals(kBottom)) {
		m_usedHandle = eBottom;
		SetResizeIncrements(0, 0, 0, 1, PR_FALSE);
		SetInfoIncrements(0, -20);
	}
	else if (locationStr.Equals(kBottomRight)) {
		m_usedHandle = eBottomRight;
		SetResizeIncrements(0, 0, 1, 1, PR_FALSE);
		SetInfoIncrements(-20, -20);
	}

	// make the shadow appear
	m_ResizingShadow->RemoveAttribute(NS_LITERAL_STRING("class"));

	// position it
	SetShadowPosition(m_ResizingShadow, m_ResizingObject, m_ResizedObjectX, m_ResizedObjectY);

	SetStylePropertyPixels(m_ResizingShadow, NS_LITERAL_STRING("width"), m_ResizedObjectWidth);
	SetStylePropertyPixels(m_ResizingShadow, NS_LITERAL_STRING("height"),	m_ResizedObjectHeight);
	
	// add a mouse move listener to the editor
	if (m_MouseMotionListener) {
		return NS_OK;
	}
	
	m_MouseMotionListener = new VpeResizerMouseMotionListener(this);
	if (!m_MouseMotionListener) {return NS_ERROR_NULL_POINTER;}
	
	nsCOMPtr<nsIDOMEventReceiver> erP;
	nsresult res = GetDOMEventReceiver(getter_AddRefs(erP));
	if (NS_SUCCEEDED(res) && erP)
	{
		res = erP->AddEventListener(NS_LITERAL_STRING("mousemove"), m_MouseMotionListener, PR_TRUE);
		if (NS_FAILED(res)) {
			HandleEventListenerError();
			return res;
		}

		res = erP->AddEventListener(NS_LITERAL_STRING("mouseup"), m_MouseListener, PR_TRUE);
		if (NS_FAILED(res)) {
			HandleEventListenerError();
			return res;
		}
	}
	else
		HandleEventListenerError();
	
	return res;
}

nsresult 
VpeResizer::GetDOMEventReceiver(nsIDOMEventReceiver **aEventReceiver) 
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

void
VpeResizer::HandleEventListenerError()
{
	m_MouseMotionListener = nsnull;
}

nsresult
VpeResizer::CreateShadow(nsIDOMElement ** aReturn, nsIDOMNode * aParentNode,
						 nsIDOMElement * aOriginalObject)
{
	// let's create an image through the element factory
	nsAutoString name;
//	if (nsHTMLEditUtils::IsImage(aOriginalObject))
//		name = NS_LITERAL_STRING("img");
//	else
		name = NS_LITERAL_STRING("span");
	nsresult res = CreateAnonymousElement(name,
			aParentNode,
			NS_LITERAL_STRING("mozResizingShadow"),
			PR_TRUE,
			aReturn);
	
	if (!*aReturn)
		return NS_ERROR_FAILURE;
	
	return res;
}

nsresult
VpeResizer::SetShadowPosition(nsIDOMElement * aShadow,
                                 nsIDOMElement * aOriginalObject,
                                 PRInt32 aOriginalObjectX,
                                 PRInt32 aOriginalObjectY)
{
	SetAnonymousElementPosition(aOriginalObjectX, aOriginalObjectY, aShadow);
/*	
	if (nsHTMLEditUtils::IsImage(aOriginalObject)) {
		nsAutoString imageSource;
		nsresult res = aOriginalObject->GetAttribute(NS_LITERAL_STRING("src"),
			imageSource);
		if (NS_FAILED(res)) return res;
		res = aShadow->SetAttribute(NS_LITERAL_STRING("src"), imageSource);
		if (NS_FAILED(res)) return res;
	}
*/
	return NS_OK;
}

nsresult
VpeResizer::SetAnonymousElementPosition(PRInt32 aX, PRInt32 aY,nsIDOMElement *aElement)
{
	nsresult res = SetStylePropertyPixels(aElement, NS_LITERAL_STRING("left"), aX);
	if (NS_FAILED(res)) {
		return res;
	}

	res = SetStylePropertyPixels(aElement, NS_LITERAL_STRING("top"), aY);

	return res;
}

nsresult
VpeResizer::SetStylePropertyPixels(nsIDOMElement *aElement, const nsAString & aProperty,
								   PRInt32 aValue) {
	nsAutoString value;
	value.AppendInt(aValue);
#ifdef _DEBUG
//	cout << "C:VpeResizer::SetStylePropertyPixels| " << NS_LossyConvertUTF16toASCII(aProperty).get() << ":" << NS_LossyConvertUTF16toASCII(value).get() << endl;
#endif
	return SetStyle(aElement, aProperty, value + NS_LITERAL_STRING("px"));
}

PRInt32
VpeResizer::GetNewResizingX(PRInt32 aX, PRInt32 aY)
{
	PRInt32 resized = m_ResizedObjectX +
		GetNewResizingIncrement(aX, aY, kX) * m_XIncrementFactor;
	PRInt32 max =   m_ResizedObjectX + m_ResizedObjectWidth;
	return PR_MIN(resized, max);
}

PRInt32
VpeResizer::GetNewResizingY(PRInt32 aX, PRInt32 aY)
{
	PRInt32 resized = m_ResizedObjectY +
		GetNewResizingIncrement(aX, aY, kY) * m_YIncrementFactor;
	PRInt32 max =   m_ResizedObjectY + m_ResizedObjectHeight;
	return PR_MIN(resized, max);
}

PRInt32
VpeResizer::GetNewResizingWidth(PRInt32 aX, PRInt32 aY)
{
	PRInt32 resized = m_ResizedObjectWidth +
		GetNewResizingIncrement(aX, aY, kWidth) *
		m_WidthIncrementFactor;
	return PR_MAX(resized, 1);
}

PRInt32
VpeResizer::GetNewResizingHeight(PRInt32 aX, PRInt32 aY)
{
	PRInt32 resized = m_ResizedObjectHeight +
		GetNewResizingIncrement(aX, aY, kHeight) *
		m_HeightIncrementFactor;
	return PR_MAX(resized, 1);
}

PRInt32
VpeResizer::GetNewResizingIncrement(PRInt32 aX, PRInt32 aY, PRInt32 aID)
{
	PRInt32 result = 0;
	switch (aID) {
	case kX:
	case kWidth:
		result = aX - m_OriginalX;
		break;
	case kY:
	case kHeight:
		result = aY - m_OriginalY;
		break;
	}
	return result;
}

void
VpeResizer::SetResizeIncrements(PRInt32 aX, PRInt32 aY,
                                  PRInt32 aW, PRInt32 aH,
                                  PRBool aPreserveRatio)
{
	m_XIncrementFactor = aX;
	m_YIncrementFactor = aY;
	m_WidthIncrementFactor = aW;
	m_HeightIncrementFactor = aH;
	//mPreserveRatio = aPreserveRatio;
}

void
VpeResizer::HideShadowAndInfo()
{
	if (m_ResizingShadow)
		m_ResizingShadow->SetAttribute(NS_LITERAL_STRING("class"), NS_LITERAL_STRING("hidden"));
	if (m_ResizingInfo)
		m_ResizingInfo->SetAttribute(NS_LITERAL_STRING("class"), NS_LITERAL_STRING("hidden"));
}

void
VpeResizer::EndResizing(PRInt32 aClientX, PRInt32 aClientY)
{
	if (!m_ResizingObject) {
		return;
	}

	if (m_ActiveHandle) {
		m_ActiveHandle->RemoveAttribute(NS_LITERAL_STRING("_moz_activated"));
		m_ActiveHandle = nsnull;
	}

	PRInt32 left   = GetNewResizingX(aClientX, aClientY);
	PRInt32 top    = GetNewResizingY(aClientX, aClientY);
	PRInt32 width  = GetNewResizingWidth(aClientX, aClientY);
	PRInt32 height = GetNewResizingHeight(aClientX, aClientY);
	
	PRInt32 listenersCount = objectResizeEventListeners.Count();
	if (listenersCount) {
		nsCOMPtr<IVpeResizeListener> listener;
		PRInt32 index;
		for (index = 0; index < listenersCount; index++) {
			listener = objectResizeEventListeners[index];
			listener->OnEndResizing(m_usedHandle,top,left,width,height,m_ResizingObject);
		}
	}
	
	m_usedHandle = 0;
	//m_ResizedObjectWidth  = width;
	//m_ResizedObjectHeight = height;
}

nsresult
VpeResizer::CreateResizingInfo(nsIDOMElement ** aReturn, nsIDOMNode * aParentNode)
{
	// let's create an info box through the element factory
	nsresult res = CreateAnonymousElement(NS_LITERAL_STRING("span"),
		aParentNode,
		NS_LITERAL_STRING("mozResizingInfo"),
		PR_TRUE,
		aReturn);
	
	if (!*aReturn)
		return NS_ERROR_FAILURE;
	
	return res;
}

void
VpeResizer::SetInfoIncrements(PRInt8 aX, PRInt8 aY)
{
	m_InfoXIncrement = aX;
	m_InfoYIncrement = aY;
}

nsresult
VpeResizer::SetResizingInfoPosition(PRInt32 aX, PRInt32 aY, PRInt32 aW, PRInt32 aH)
{
/*
	nsCOMPtr<nsIDOMDocument> domDoc = do_QueryReferent(m_DocWeak);
	if (NS_FAILED(res)) {
		return res;
	}
	if (!domDoc) {
		return NS_ERROR_FAILURE;
	}
	
	nsCOMPtr<nsIDocument> doc = do_QueryInterface(domDoc);
	if (!doc) {
		return NS_ERROR_UNEXPECTED;
	}

	// get the root content
	nsCOMPtr<nsIDOMNSHTMLElement> nsElement = do_QueryInterface(doc->GetRootContent());
	if (!nsElement) {
		return NS_ERROR_NULL_POINTER;
	}

	// let's get the size of the document
	PRInt32 w, h;
	nsElement->GetOffsetWidth(&w);
	nsElement->GetOffsetHeight(&h);

	if (m_InfoXIncrement < 0)
		aX = w - aX ;
	if (m_InfoYIncrement < 0)
		aY = h - aY;

	NS_NAMED_LITERAL_STRING(rightStr, "right");
	NS_NAMED_LITERAL_STRING(leftStr, "left");
	NS_NAMED_LITERAL_STRING(topStr, "top");
	NS_NAMED_LITERAL_STRING(bottomStr, "bottom");
	SetStylePropertyPixels(m_ResizingInfo,
							(m_InfoXIncrement < 0) ? rightStr : leftStr,
							aX + PR_ABS(m_InfoXIncrement));
	SetStylePropertyPixels(m_ResizingInfo,
							(m_InfoYIncrement < 0) ? bottomStr : topStr,
							aY + PR_ABS(m_InfoYIncrement));

	mHTMLCSSUtils->RemoveCSSProperty(m_ResizingInfo,
								   (mInfoXIncrement >= 0) ? rightStr  : leftStr);
	mHTMLCSSUtils->RemoveCSSProperty(mResizingInfo,
								   (mInfoYIncrement >= 0) ? bottomStr  : topStr);

	// let's make sure the info box does not go beyond the limits of the viewport
	nsAutoString value;
	float f;
	nsCOMPtr<nsIAtom> unit;
	if (mInfoXIncrement < 0) {
	mHTMLCSSUtils->GetComputedProperty(mResizingInfo, nsEditProperty::cssLeft, value);
	mHTMLCSSUtils->ParseLength(value, &f, getter_AddRefs(unit));
	if (f <= 0) {
	  mHTMLCSSUtils->SetCSSPropertyPixels(mResizingInfo, leftStr, 0);
	  mHTMLCSSUtils->RemoveCSSProperty(mResizingInfo,
									   rightStr);
	}
	}
	if (mInfoYIncrement < 0) {
	mHTMLCSSUtils->GetComputedProperty(mResizingInfo, nsEditProperty::cssTop, value);
	mHTMLCSSUtils->ParseLength(value, &f, getter_AddRefs(unit));
	if (f <= 0) {
	  mHTMLCSSUtils->SetCSSPropertyPixels(mResizingInfo, topStr, 0);
	  mHTMLCSSUtils->RemoveCSSProperty(mResizingInfo,
									   bottomStr);
	}
	}

	nsCOMPtr<nsIDOMNode> textInfo;
	nsresult res = mResizingInfo->GetFirstChild(getter_AddRefs(textInfo));
	if (NS_FAILED(res)) return res;
	nsCOMPtr<nsIDOMNode> junk;
	if (textInfo) {
	res = mResizingInfo->RemoveChild(textInfo, getter_AddRefs(junk));
	if (NS_FAILED(res)) return res;
	textInfo = nsnull;
	junk = nsnull;
	}

	nsAutoString widthStr, heightStr, diffWidthStr, diffHeightStr;
	widthStr.AppendInt(aW);
	heightStr.AppendInt(aH);
	PRInt32 diffWidth  = aW - mResizedObjectWidth;
	PRInt32 diffHeight = aH - mResizedObjectHeight;
	if (diffWidth > 0)
	diffWidthStr = NS_LITERAL_STRING("+");
	if (diffHeight > 0)
	diffHeightStr = NS_LITERAL_STRING("+");
	diffWidthStr.AppendInt(diffWidth);
	diffHeightStr.AppendInt(diffHeight);

	nsAutoString info(widthStr + NS_LITERAL_STRING(" x ") + heightStr +
					NS_LITERAL_STRING(" (") + diffWidthStr +
					NS_LITERAL_STRING(", ") + diffHeightStr +
					NS_LITERAL_STRING(")"));

	nsCOMPtr<nsIDOMText> nodeAsText;
	res = domdoc->CreateTextNode(info, getter_AddRefs(nodeAsText));
	if (NS_FAILED(res)) return res;
	textInfo = do_QueryInterface(nodeAsText);
	res =  mResizingInfo->AppendChild(textInfo, getter_AddRefs(junk));
	if (NS_FAILED(res)) return res;

	PRBool hasClass = PR_FALSE;
	if (NS_SUCCEEDED(mResizingInfo->HasAttribute(NS_LITERAL_STRING("class"), &hasClass )) && hasClass)
	res = mResizingInfo->RemoveAttribute(NS_LITERAL_STRING("class"));

	return res;
*/
	return NS_OK;
}
