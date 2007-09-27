// VpeDragListener.cpp: implementation of the VpeDragListener class.
//
//////////////////////////////////////////////////////////////////////

#include "DragListener.h"

#include "IVpeDragDropListener.h"


#include "nsIServiceManager.h"
#include "nsIDragService.h"
#include "nsIDOMMouseEvent.h"
#include "nsIDOMEventTarget.h"
#include "nsIDOMElement.h"
#include "nsIDOMNode.h"
#include "nsIDOMNSEvent.h"
#include "nsIDOMNSUIEvent.h"
#include "nsIEventStateManager.h"
#include "nsIFrame.h"
#include "nsIPresContext.h"
#include "nsISupportsArray.h"
#include "nsICaret.h"
#include "nsIPresShell.h"
#include "nsIDOMNSUIEvent.h"
#include "nsString.h"
#include "nsITransferable.h"
#include "nsISupportsPrimitives.h"
#include "nsReadableUtils.h"
#include "nsCRT.h"
#include "nsIURI.h"
#include "nsIURL.h"
#include "nsNetUtil.h"
#include "nsUnitConversion.h"
#include "nsIDOMNamedNodeMap.h"
#include "nsIDOMNSDocument.h"
#include "nsIDOMDocument.h"
#include "nsIBoxObject.h"
#include "nsIScrollableView.h"

#ifdef _DEBUG
#include <iostream>
using namespace std;
#endif

#define DND_TYPE_INTERNAL			0
#define DND_TYPE_VPE_PALETTE		1
#define DND_TYPE_OTHER				2
			
#define kVpeModelFlavor				"vpe/model"
#define kVpeModelTransport			"vpe/model"
#define kHTMLContext				"text/_moz_htmlcontext"

#define	MOZ_ANONCLASS				"_MOZ_ANONCLASS"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

DragListener::DragListener(nsIPresShell * aPresShell, nsIDOMDocument * aDOMDocument, IVpeDragDropListener *aListener,const nsAString &aPathSeparator)
:nsIDOMDragListener()
,m_VpeDragDropListener(aListener)
,m_PresShell(aPresShell)
//,m_DOMDocument(aDOMDocument)
,m_scrollView(nsnull)
,m_PathSeparator(aPathSeparator)
,m_DocEltBox(nsnull)
,m_p2t(0.0)
,m_clientX(0)
,m_clientY(0)
#ifdef _DEBUG
,m_counter(0)
#endif
{
	nsCOMPtr<nsIDOMElement> elt = nsnull;

	aPresShell->GetPresContext()->GetPixelsToTwips(&m_p2t);
	nsresult rv = aPresShell->GetViewManager()->GetRootScrollableView( &m_scrollView );
	if (NS_FAILED(rv)) {
#ifdef _DEBUG
			cout << "C:DragListener::DragListener| Can't get Scrollable View" <<  endl;
#endif
	} else {
#ifdef _DEBUG
			cout << "C:DragListener::DragListener| Scrollable View was gotten" <<  endl;
#endif
	}

	rv = aDOMDocument->GetDocumentElement( getter_AddRefs(elt) );
	if (NS_FAILED(rv)) {
#ifdef _DEBUG
			cout << "C:DragListener::DragListener| Can't get element from Document" <<  endl;
#endif
	} else {
		nsCOMPtr<nsIDOMNSDocument> nsDoc = do_QueryInterface( aDOMDocument, &rv );
		rv = nsDoc->GetBoxObjectFor( elt, getter_AddRefs(m_DocEltBox) );
		if (NS_FAILED(rv)) {
#ifdef _DEBUG
			cout << "C:DragListener::DragListener| Can't get box from nsDocument" <<  endl;
#endif
		}
	}
}

DragListener::~DragListener()
{

}

NS_IMPL_ISUPPORTS2(DragListener, nsIDOMEventListener, nsIDOMDragListener)

NS_IMETHODIMP
DragListener::HandleEvent(nsIDOMEvent* aEvent)
{
#ifdef _DEBUG
	cout << "C:DragListener::HandleEvent|" << endl;
#endif
	return NS_OK;
}


NS_IMETHODIMP
DragListener::DragGesture(nsIDOMEvent* aDragEvent)
{
#ifdef _DEBUG
	cout << "C:DragListener::DragGesture| (Internal DnD)" << endl;
#endif
	nsresult res;

	nsCOMPtr<nsIDOMMouseEvent> mouseEvent = do_QueryInterface(aDragEvent, &res);
	if (!mouseEvent) {
		return res;
	}

	nsCOMPtr<nsIDOMEventTarget> target = nsnull;
	res = aDragEvent->GetTarget(getter_AddRefs(target));
	if (!target) {
#ifdef _DEBUG
		cout << "C:DragListener::DragGesture| There are no interface nsIDOMEventTarget" << endl;
#endif
		return res;
	}
	
	nsCOMPtr<nsIDOMNode> node = do_QueryInterface(target, &res);
	if (!node) {
#ifdef _DEBUG
		cout << "C:DragListener::DragGesture| There are no interface nsIDOMNode" << endl;
#endif
		return res;
	}
	
	nsAutoString nodeName;
	res = node->GetNodeName(nodeName);
	if (NS_FAILED(res)) {
#ifdef _DEBUG
		cout << "C:DragListener::DragGesture| Can't get node name" << endl;
#endif
		return res;
	}

#ifdef _DEBUG
	cout << "C:DragListener::DragGesture| Draged node is " << NS_LossyConvertUTF16toASCII(nodeName).get() << endl;
#endif

	// If node is not dragable, DragSession will not start.
	PRBool isDragable = PR_FALSE;
	res = IsDragableNode(node, isDragable); 
	if (!isDragable) {
#ifdef _DEBUG
			cout << "C:DragListener::DragGesture| not dragable" << endl;
#endif
		return NS_OK;
	}
#ifdef _DEBUG
	else {
			cout << "C:DragListener::DragGesture| dragable" << endl;
	}
#endif

	PRInt32 canDrag = PR_FALSE;
	nsCOMPtr<nsIDOMNode> parent;
	PRInt32 offset;
	// DragGesture may be only on internal drag'n'drop
	res = m_VpeDragDropListener->CanDrag(aDragEvent, &canDrag, getter_AddRefs(parent), &offset);
	if (NS_FAILED(res)) {
		return res;
	}
#ifdef _DEBUG
	cout << "C:DragListener::DragGesture| can" << ( canDrag ? "":"'t") << " drag" << endl;
#endif

	if (m_Caret && m_CaretDrawn) {
		m_Caret->SetCaretVisible(PR_FALSE);
		m_Caret->EraseCaret();
		m_CaretDrawn = PR_FALSE;
	}
	
	if (canDrag) {
		nsCOMPtr<nsITransferable> trans;
		res = CreateTransferable(getter_AddRefs(trans));
		if (NS_FAILED(res)) {
			return res;
		}

		nsCOMPtr<nsISupportsArray> transArray(do_CreateInstance("@mozilla.org/supports-array;1"));
		if ( !transArray ) {
			return NS_ERROR_FAILURE;
		}
		transArray->InsertElementAt(trans, 0);

		// kick off the drag
		nsCOMPtr<nsIDragService> dragService(do_GetService("@mozilla.org/widget/dragservice;1"));
		if ( !dragService ) {
			return NS_ERROR_FAILURE;
		}
		res = dragService->InvokeDragSession(node, transArray, nsnull, nsIDragService::DRAGDROP_ACTION_COPY +
										nsIDragService::DRAGDROP_ACTION_MOVE + nsIDragService::DRAGDROP_ACTION_LINK);
		if (NS_FAILED(res)) {
			return res;
		}

		if (m_Caret && parent) {
			m_Caret->DrawAtPosition(parent, offset);
			m_CaretDrawn = PR_TRUE;
		}

	}
/*	else {
		aDragEvent->StopPropagation();
		aDragEvent->PreventDefault();
	}
*/

	aDragEvent->StopPropagation();
	aDragEvent->PreventDefault();

	return NS_OK;
}


NS_IMETHODIMP
DragListener::DragEnter(nsIDOMEvent* aDragEvent)
{
#ifdef _DEBUG
	cout << "C:DragListener::DragEnter|" << endl;
#endif

	if (m_PresShell)
	{
		if (!m_Caret)
		{
			m_Caret = do_CreateInstance("@mozilla.org/layout/caret;1");
			if (m_Caret)
			{
				m_Caret->Init(m_PresShell);
				m_Caret->SetCaretReadOnly(PR_TRUE);
				m_Caret->SetCaretWidth(2);
			}
			m_CaretDrawn = PR_FALSE;
		}
	}
	
//	return DragOver(aDragEvent);
	return NS_OK;
}


NS_IMETHODIMP
DragListener::DragOver(nsIDOMEvent* aDragEvent)
{
#ifdef _DEBUG
	cout << "C:DragListener::DragOver| << begin >> (" << ++m_counter << ")" << endl;
#endif
	nsresult rv;
	nsCOMPtr<nsIDragService> dragService = do_GetService("@mozilla.org/widget/dragservice;1", &rv);
	if (!dragService) {
		return rv;
	}
	
	// does the drag have flavors we can accept?
	nsCOMPtr<nsIDragSession> dragSession;
	dragService->GetCurrentSession(getter_AddRefs(dragSession));
	if (!dragSession) {
		return NS_ERROR_FAILURE;
	}

	PRInt32 DnDType = DND_TYPE_OTHER;
	rv = GetDnDType(DnDType);
	if (NS_FAILED(rv)) {
		return rv;
	}

	PRInt32 canDrop = PR_FALSE;
	
	nsCOMPtr<nsIDOMNode> parent;
	PRInt32 offset;

	if(DnDType == DND_TYPE_INTERNAL ) {
#ifdef _DEBUG
		cout << "C:DragListener::DragOver| (Internal DnD)" << endl;
#endif
		rv = m_VpeDragDropListener->CanDrop(aDragEvent, &canDrop, getter_AddRefs(parent), &offset);
		if (NS_FAILED(rv)) {
			return rv;
		}
	} else {
#ifdef _DEBUG
		cout << "C:DragListener::DragOver| ("<< (DnDType == DND_TYPE_VPE_PALETTE ? "VPE Palette" : "External") <<" DnD)" << endl;
#endif
		nsString flavor;
		nsString transData;
// Edward		
//		rv = GetData(flavor, transData, DnDType);
		rv = GetFlavor(flavor, DnDType);

		if (NS_FAILED(rv)) {
#ifdef _DEBUG
			cout << "C:DragListener::DragOver| GetData is failed!" << endl;
#endif
			return rv;
		}

#ifdef _DEBUG
		cout << "C:DragListener::DragOver| flavor is " << ( flavor.IsEmpty() ? "empty" : NS_LossyConvertUTF16toASCII(flavor).get()) << endl;
		cout << "C:DragListener::DragOver| transData is " << (transData.IsEmpty() ? "empty" : NS_LossyConvertUTF16toASCII(transData).get()) << endl;
#endif

// Edward
//		if (!flavor.IsEmpty() && !transData.IsEmpty()) {
		if (!flavor.IsEmpty()) {
			rv = m_VpeDragDropListener->CanDropExternal(aDragEvent, flavor, transData, &canDrop, getter_AddRefs(parent), &offset);
			if (NS_FAILED(rv)) {
#ifdef _DEBUG
			cout << "C:DragListener::DragOver| CanDropExternal is failed!" << endl;
#endif
				return rv;
			}
#ifdef _DEBUG
			else {
				cout << "C:DragListener::DragOver| Data is : " << NS_LossyConvertUTF16toASCII(transData).get() << endl;
			}
#endif
		}
		
	}

#ifdef _DEBUG
	cout << "C:DragListener::DragOver| Can" << (canDrop ? "" : "'t") << " drop" << endl;
#endif
	
	dragSession->SetCanDrop(canDrop);
	
	// We need to consume the event to prevent the browser's
	// default drag listeners from being fired. (Bug 199133)
	
	if (m_Caret && m_CaretDrawn) {
		m_Caret->SetCaretVisible(PR_FALSE);
		m_Caret->EraseCaret();
		m_CaretDrawn = PR_FALSE;
	}
	
	if (canDrop) {
		if (m_Caret && parent) {
			m_Caret->DrawAtPosition(parent, offset);
			m_CaretDrawn = PR_TRUE;
		}
	}

/************************************* Scrolling begin **************************************************/
	nsCOMPtr<nsIDOMMouseEvent> mouseEvent ( do_QueryInterface(aDragEvent) );
	if (mouseEvent) {
		PRInt32 clientX = 0;
		PRInt32 clientY = 0;
		mouseEvent->GetClientX( &clientX );
		mouseEvent->GetClientY( &clientY );
		clientX = NSToIntRound( clientX * m_p2t );
		clientY = NSToIntRound( clientY * m_p2t );

		if (clientX != m_clientX || clientY != m_clientY) {
			m_clientX = clientX;
			m_clientY = clientY;
		} else {
#ifdef _DEBUG
			cout << "C:DragListener::DragOver| Client X=" << clientX << " Y=" << clientY << endl;
#endif
			//if (m_DocEltBox != nsnull) {
			//	PRInt32 boxX = 0;
			//	PRInt32 boxY = 0;
			//	PRInt32 boxHeight = 0;
			//	PRInt32 boxWidth = 0;

			//	m_DocEltBox->GetX( &boxX );
			//	m_DocEltBox->GetY( &boxY );
			//	m_DocEltBox->GetHeight( &boxHeight );
			//	m_DocEltBox->GetWidth( &boxWidth );

			//	boxX = NSToIntRound( boxX * m_p2t );
			//	boxY = NSToIntRound( boxY * m_p2t );
			//	boxHeight = NSToIntRound( boxHeight * m_p2t );
			//	boxWidth = NSToIntRound( boxWidth * m_p2t );


			if (m_scrollView) {
				const nsIView *clipView = nsnull;
				m_scrollView->GetClipView( &clipView );
				nsRect rectView = clipView->GetBounds();
				
				PRInt32 minXcoord = 0;
				PRInt32 maxXcoord = 0;
				PRInt32 minYcoord = 0;
				PRInt32 maxYcoord = 0;

				minXcoord = NSToIntRound( rectView.width / 10.0 );
				maxXcoord = rectView.width - minXcoord;

				minYcoord = NSToIntRound( rectView.height / 10.0 );
				maxYcoord = rectView.height - minYcoord;
				
				PRInt32 numLineX = 0;
				PRInt32 numLineY = 0;

				if (clientX < minXcoord) numLineX = -1;
				else if (clientX > maxXcoord) numLineX = 1;

				if (clientY < minYcoord) numLineY = -1;
				else if (clientY > maxYcoord) numLineY = 1;

				if (numLineX != 0 || numLineY != 0) {
					m_scrollView->ScrollByLines( numLineX, numLineY );
				}
			}
		}

//#ifdef _DEBUG
//				cout << "C:DragListener::DragOver| ClientX = " << clientX << " ClientY = " << clientY << endl;
//#endif
//
//#ifdef _DEBUG
//				cout << "C:DragListener::DragOver| Box X = " << boxX << " Y = " << boxY << endl;
//				cout << "C:DragListener::DragOver| Box height = " << boxHeight << " width = " << boxWidth << endl;
//#endif
//			}
//		}
	}
/**************************************** Scrolling end *********************************************/


	aDragEvent->StopPropagation();
	aDragEvent->PreventDefault(); // consumed

#ifdef _DEBUG
	cout << "C:DragListener::DragOver| << end >> " << endl << endl;
#endif

	return NS_OK;
}


NS_IMETHODIMP
DragListener::DragExit(nsIDOMEvent* aDragEvent)
{
#ifdef _DEBUG
	cout << "C:DragListener::DragExit|" << endl;
#endif
	if (m_Caret && m_CaretDrawn) {
		m_Caret->SetCaretVisible(PR_FALSE);
		m_Caret->EraseCaret();
		m_CaretDrawn = PR_FALSE;
	}

	return NS_OK;
}

NS_IMETHODIMP
DragListener::DragDrop(nsIDOMEvent* aDragEvent)
{
	aDragEvent->StopPropagation();
	aDragEvent->PreventDefault();


	if (m_Caret && m_CaretDrawn) {
		m_Caret->SetCaretVisible(PR_FALSE);
		m_Caret->EraseCaret();
		m_CaretDrawn = PR_FALSE;
	}
#ifdef _DEBUG
	cout << "C:DragListener::DragDrop|" << endl;
#endif
	nsresult res;
	nsCOMPtr<nsIDOMMouseEvent> mouseEvent = do_QueryInterface(aDragEvent, &res);
	if (!mouseEvent) {
		return res;
	}
	
	PRInt32 DnDType = DND_TYPE_OTHER;
	res = GetDnDType(DnDType);
	if (NS_FAILED(res)) {
#ifdef _DEBUG
	cout << "C:DragListener::DragDrop| GetDnDType failed" << endl;
#endif
		return res;
	}
	
	nsCOMPtr<nsIDOMNode> parent;
	PRInt32 offset;

	if (DnDType == DND_TYPE_INTERNAL) {
#ifdef _DEBUG
		cout << "C:DragListener::DragDrop| (Internal DnD)" << endl;
#endif

//		nsIDOMNode* rangeParent = nsnull;
//		PRInt32 rangeOffset;
//		GetRangeParentAndOffset(aDragEvent, &rangeParent, &rangeOffset);		
		
		res = m_VpeDragDropListener->Drop(aDragEvent, getter_AddRefs(parent), &offset);
		if (NS_FAILED(res)) {
			return res;
		}
	} else {
#ifdef _DEBUG
		cout << "C:DragListener::DragDrop| (" << (DnDType == DND_TYPE_VPE_PALETTE ? "VPE Palette" : "External" ) << " DnD)" << endl;
#endif
		nsString flavor;
		nsString transData;
		res = GetData(flavor, transData, DnDType);
		if (NS_FAILED(res)) {
#ifdef _DEBUG
			cout << "C:DragListener::DragDrop| GetData is Ok!" << endl;
#endif
			return res;
		}
#ifdef _DEBUG
		else {
			cout << "C:DragListener::DragDrop| Data is : " << NS_LossyConvertUTF16toASCII(transData).get() << endl;
		}
#endif
			
		res = m_VpeDragDropListener->DropExternal(aDragEvent, flavor, transData, getter_AddRefs(parent), &offset);
		if (NS_FAILED(res)) {
#ifdef _DEBUG
			cout << "C:DragListener::DragDrop| DropExternal is failed!" << endl;
#endif
			return res;
		}
	}

/*
	if (m_Caret) {
		if (parent) {
			if (m_CaretDrawn) {
				m_Caret->EraseCaret();
			}
			m_Caret->DrawAtPosition(parent, offset);
			m_CaretDrawn = PR_TRUE;
		} else {
			m_Caret->EraseCaret();
			m_CaretDrawn = PR_FALSE;
		}
	}
*/
	return NS_OK;
}

nsresult
DragListener::PrepareTransferable(nsITransferable **aTransferable,
									  PRInt32 aDnDType)
{
	// Create generic Transferable for getting the data
	nsresult rv = nsComponentManager::CreateInstance("@mozilla.org/widget/transferable;1", nsnull, 
		NS_GET_IID(nsITransferable), 
		(void**)aTransferable);
	if (NS_FAILED(rv))
		return rv;
	
	// Get the nsITransferable interface for getting the data from the clipboard
	if (aTransferable)
	{
		switch(aDnDType) {
		case DND_TYPE_INTERNAL:
			(*aTransferable)->AddDataFlavor(kNativeHTMLMime);
			break;
		case DND_TYPE_VPE_PALETTE:
			(*aTransferable)->AddDataFlavor(kVpeModelFlavor);
			break;
		case DND_TYPE_OTHER:
// Edward
//			(*aTransferable)->AddDataFlavor(kTextMime);
			(*aTransferable)->AddDataFlavor(kUnicodeMime);
			(*aTransferable)->AddDataFlavor(kHTMLMime);
			(*aTransferable)->AddDataFlavor(kFileMime);
			(*aTransferable)->AddDataFlavor(kURLMime);
			break;
		default:			
			(*aTransferable)->AddDataFlavor(kUnicodeMime);
			
/*			
			(*aTransferable)->AddDataFlavor(kAOLMailMime);
			(*aTransferable)->AddDataFlavor(kPNGImageMime);
			(*aTransferable)->AddDataFlavor(kGIFImageMime);
			(*aTransferable)->AddDataFlavor(kURLDataMime);
			(*aTransferable)->AddDataFlavor(kURLDescriptionMime);
			(*aTransferable)->AddDataFlavor(kNativeImageMime);
			(*aTransferable)->AddDataFlavor(kNativeHTMLMime);
			(*aTransferable)->AddDataFlavor(kFilePromiseURLMime);
			(*aTransferable)->AddDataFlavor(kFilePromiseMime);
			(*aTransferable)->AddDataFlavor(kFilePromiseDirectoryMime);

#define kTextMime                   "text/plain"
#define kUnicodeMime                "text/unicode"
#define kHTMLMime                   "text/html"
#define kAOLMailMime                "AOLMAIL"
#define kPNGImageMime               "image/png"
#define kJPEGImageMime              "image/jpg"
#define kGIFImageMime               "image/gif"
#define kFileMime                   "application/x-moz-file"
#define kURLMime                    "text/x-moz-url"        // data contains url\ntitle
#define kURLDataMime                "text/x-moz-url-data"   // data contains url only
#define kURLDescriptionMime         "text/x-moz-url-desc"   // data contains description
#define kNativeImageMime            "application/x-moz-nativeimage"
#define kNativeHTMLMime             "application/x-moz-nativehtml"
// the source URL for a file promise
#define kFilePromiseURLMime         "application/x-moz-file-promise-url"
// a dataless flavor used to interact with the OS during file drags
#define kFilePromiseMime            "application/x-moz-file-promise"
// a synthetic flavor, put into the transferable once we know the destination directory of a file drag
#define kFilePromiseDirectoryMime   "application/x-moz-file-promise-dir"
*/		
		}
	}
	
	return NS_OK;
}

nsresult
DragListener::GetDnDType(PRInt32 &aDnDType)
{
	nsresult rv;
	nsCOMPtr<nsIDragService> dragService = 
		do_GetService("@mozilla.org/widget/dragservice;1", &rv);
	if (NS_FAILED(rv)) {
		return rv;
	}
	nsCOMPtr<nsIDragSession> dragSession;
	rv = dragService->GetCurrentSession(getter_AddRefs(dragSession)); 
	if (!dragSession) {
		return rv;
	}

	// find out if we have our internal html flavor on the clipboard.  We don't want to mess
	// around with cfhtml if we do.
	PRBool bHavePrivateHTMLFlavor = PR_FALSE;
	rv = dragSession->IsDataFlavorSupported(kHTMLContext, &bHavePrivateHTMLFlavor);
	if (NS_FAILED(rv)) {
#ifdef _DEBUG
		cout << "GetDnDType:IsDataFlavorSupported(kHTMLContext failed" << endl;
#endif
		return rv;
	}
	if (bHavePrivateHTMLFlavor) {
#ifdef _DEBUG
		cout << "GetDnDType: bHavePrivateHTMLFlavor true" << endl;
#endif
		aDnDType = DND_TYPE_INTERNAL;
	} else {
		PRBool bHaveVpeModelFlavor = PR_FALSE;
		rv = dragSession->IsDataFlavorSupported(kVpeModelTransport,&bHaveVpeModelFlavor);
		if (NS_FAILED(rv)) {
#ifdef _DEBUG
		cout << "GetDnDType:IsDataFlavorSupported(kVpeModelTransport failed" << endl;
#endif
			return rv;
		}
		if (bHaveVpeModelFlavor) {
#ifdef _DEBUG
		cout << "GetDnDType: bHaveVpeModelFlavor true" << endl;
#endif
			aDnDType = DND_TYPE_VPE_PALETTE;
		} else {
			aDnDType = DND_TYPE_OTHER;
		}
	}
	
	return NS_OK;
}

nsresult
DragListener::GetFlavor(nsAString &aFlavor, PRInt32 aDnDType)
{
	switch(aDnDType) {
	case DND_TYPE_INTERNAL:
		aFlavor.Assign(NS_ConvertUTF8toUCS2(kNativeHTMLMime));
		break;
	case DND_TYPE_VPE_PALETTE:
		aFlavor.Assign(NS_ConvertUTF8toUCS2(kVpeModelFlavor));
		break;
	default:			
		aFlavor.Assign(NS_ConvertUTF8toUCS2(kFileMime));
	}
	return NS_OK;
}

nsresult
DragListener::GetData(nsAString &aFlavor, nsAString &aData, PRInt32 aDnDType)
{
#ifdef _DEBUG
	cout << "C:DragListener::GetData| << begin >>" << endl;
#endif
	nsresult rv;
	nsAutoString data(NS_LITERAL_STRING(""));
	nsAutoString flavor(NS_LITERAL_STRING(""));

	nsCOMPtr<nsIDragService> dragService = 
		do_GetService("@mozilla.org/widget/dragservice;1", &rv);
	if (NS_FAILED(rv)) return rv;
#ifdef _DEBUG
	cout << "C:DragListener::GetData| 1" << endl;
#endif

	nsCOMPtr<nsIDragSession> dragSession;
	rv = dragService->GetCurrentSession(getter_AddRefs(dragSession)); 
	if (!dragSession) return rv;
#ifdef _DEBUG
	cout << "C:DragListener::GetData| 2" << endl;
#endif

	// Get the nsITransferable interface for getting the data from the drop
	nsCOMPtr<nsITransferable> trans;
	rv = PrepareTransferable(getter_AddRefs(trans), aDnDType);
	if (NS_FAILED(rv)) return rv;
#ifdef _DEBUG
	cout << "C:DragListener::GetData| 3" << endl;
#endif
	
	if (!trans) return NS_OK;  // NS_ERROR_FAILURE; SHOULD WE FAIL?
#ifdef _DEBUG
	cout << "C:DragListener::GetData| 4" << endl;
#endif

	PRUint32 numItems = 0; 
	rv = dragSession->GetNumDropItems(&numItems);
#ifdef _DEBUG
	cout << "C:DragListener::GetData| 5" << endl;
#endif
	
	if (NS_FAILED(rv)) return rv;
		
#ifdef _DEBUG
	cout << "C:DragListener::GetData| GetNumDropItems numItems is " << numItems << endl;
#endif
	// Source doc is null if source is *not* the current editor document
	nsCOMPtr<nsIDOMDocument> srcdomdoc;
	rv = dragSession->GetSourceDocument(getter_AddRefs(srcdomdoc));
	if (NS_FAILED(rv)) {
#ifdef _DEBUG
	cout << "C:DragListener::GetData| GetSourceDocument is failed" << endl;
#endif
		return rv;
	}
	
	PRUint32 i; 
	for (i = 0; i < numItems; i++) {
		rv = dragSession->GetData(trans, i);
		if (NS_FAILED(rv)) {
#ifdef _DEBUG
	cout << "C:DragListener::GetData| dragSession->GetData(trans, " << i << ") is failed" << endl;
#endif
			return rv;
		}
		
		if (!trans) {
#ifdef _DEBUG
	cout << "C:DragListener::GetData| dragSession->GetData(trans, " << i << ") returned trans is null" << endl;
#endif
			return NS_OK; // NS_ERROR_FAILURE; Should we fail?
		}
		
		nsCOMPtr<nsISupports> genericDataObj;
		char *bestFlavor = nsnull;
		PRUint32 len = 0;
		if ( NS_SUCCEEDED(trans->GetAnyTransferData(&bestFlavor, getter_AddRefs(genericDataObj), &len))
			&& bestFlavor != nsnull) {
#ifdef _DEBUG
			cout << "C:DragListener::GetData| BestFlavor is: " << bestFlavor << endl;
#endif
			flavor.Assign(NS_ConvertUTF8toUCS2(bestFlavor));

			if (nsCRT::strcmp(bestFlavor,kVpeModelFlavor) == 0
					|| nsCRT::strcmp(bestFlavor,kHTMLMime) == 0
					|| nsCRT::strcmp(bestFlavor,kUnicodeMime) == 0
					|| nsCRT::strcmp(bestFlavor,kURLMime) == 0 ) {
				nsCOMPtr<nsISupportsString> textDataObj ( do_QueryInterface(genericDataObj) );
				if (textDataObj && len > 0)
				{
					nsAutoString stuffToPaste(NS_LITERAL_STRING(""));
					rv = textDataObj->GetData(stuffToPaste);
					if (NS_FAILED(rv)) {
						return rv;
					}
					
					if (!stuffToPaste.IsEmpty()) {
						data.Assign(stuffToPaste);
					}
#ifdef _DEBUG
					cout << "C:DragListener::GetData| Data is: " << NS_LossyConvertUTF16toASCII(stuffToPaste).get() << endl;
#endif
				}
			} else if (nsCRT::strcmp(bestFlavor, kFileMime) == 0) {
				nsCOMPtr<nsIFile> fileObj(do_QueryInterface(genericDataObj));
				if (fileObj && len > 0)
				{
					nsCOMPtr<nsIURI> uri;
					rv = NS_NewFileURI(getter_AddRefs(uri), fileObj);
					if (NS_FAILED(rv))
						return rv;
					
					nsCOMPtr<nsIURL> fileURL(do_QueryInterface(uri));
					if (fileURL)
					{
						nsCAutoString urltext;
						rv = fileURL->GetSpec(urltext);
						if (NS_SUCCEEDED(rv) && !urltext.IsEmpty())
						{
							if (!data.IsEmpty()) {
								data.Append(m_PathSeparator);
							}
							data.Append(NS_ConvertUTF8toUCS2(urltext));
#ifdef _DEBUG
							cout << "C:DragListener::GetData| Data is: " << urltext.get() << endl;
#endif
						}
					}
				}
				
			}
		}
#ifdef _DEBUG
		else {
			cout << "C:DragListener::GetData| trans->GetAnyTransferData(&bestFlavor, getter_AddRefs(genericDataObj), &len) is failed " << endl;
		}
#endif	
	}

	aFlavor = flavor;
	aData = data;

#ifdef _DEBUG
	cout << "C:DragListener::GetData| << end >>" << endl << endl;
#endif

	return NS_OK;
}

nsresult
DragListener::GetRangeParentAndOffset(nsIDOMEvent* aMouseEvent, nsIDOMNode** aRangeParent, PRInt32* aRangeOffset)
{
#ifdef _DEBUG
		cout << "C:GetRangeParentAndOffset   0" << endl;
#endif
	nsresult rv;
	nsCOMPtr<nsIPresContext> presContext;
	rv = m_PresShell->GetPresContext(getter_AddRefs(presContext)); 
	if (NS_FAILED(rv)) return rv;

	float p2t;
	rv = presContext->GetPixelsToTwips(&p2t);
	if (NS_FAILED(rv)) return rv;

	nsCOMPtr<nsIDOMNSUIEvent> nsuiEvent = do_QueryInterface(aMouseEvent, &rv);
	if (NS_FAILED(rv)) return rv;

	nscoord layerX;
	rv = nsuiEvent->GetLayerX(&layerX);
	if (NS_FAILED(rv)) return rv;

	PRInt32 layerY;
	rv = nsuiEvent->GetLayerY(&layerY);
//	rv = nsuiEvent->GetPageY(&layerY);
	if (NS_FAILED(rv)) return rv;

	nsCOMPtr<nsIEventStateManager> manager;
	rv = presContext->GetEventStateManager(getter_AddRefs(manager));
	if (NS_FAILED(rv)) return rv;

//	layerX = NSIntPixelsToTwips(layerX, p2t);
	layerY = NSIntPixelsToTwips(layerY, p2t);

//	nscoord
	nsIFrame* targetFrame = nsnull;
	rv = manager->GetEventTarget(&targetFrame);
	if (NS_FAILED(rv)) return rv;

	nsIFrame* childFrame = nsnull;
	rv = targetFrame->FirstChild(presContext, nsnull, &childFrame);
	if (NS_FAILED(rv)) return rv;
	
	while (nsnull != childFrame) {
		PRBool skipThisChild = (childFrame->GetStateBits() & NS_FRAME_GENERATED_CONTENT) != 0;
		
#ifdef _DEBUG
		cout << "C:GetRangeParentAndOffset   1" << endl;
#endif
		if (!skipThisChild) {
			nsIContent* childContent = childFrame->GetContent();
			
			if (childContent) {
				nsCOMPtr<nsIContent> content = childContent->GetParent();

				if (content) {
					PRInt32 childCount = content->GetChildCount();
					PRInt32 childIndex = content->IndexOf(childContent);
					
					if (childIndex < 0 || childIndex >= childCount) {
						skipThisChild = PR_TRUE;
					}
				}
			}
		}
		
		if (skipThisChild) {
			childFrame = childFrame->GetNextSibling();
			continue;
		}

		nsPoint offsetPoint(0,0);
		nsIView * childView = nsnull;
		childFrame->GetOffsetFromView(presContext, offsetPoint, &childView);

		nsRect rect = childFrame->GetRect();
		rect.x = offsetPoint.x;
		rect.y = offsetPoint.y;
    
		childFrame = childFrame->GetNextSibling();
	}
	

#ifdef _DEBUG
		cout << "C:GetRangeParentAndOffset   1" << "  " << layerY << endl;
#endif
	return NS_OK;
}

nsresult
DragListener::CreateTransferable(nsITransferable** outTrans)
{
	// now create the transferable and stuff data into it.
	nsCOMPtr<nsITransferable> trans(do_CreateInstance("@mozilla.org/widget/transferable;1"));
	if ( !trans )
		return NS_ERROR_FAILURE;
  
  // add the full html
/*
	nsCOMPtr<nsISupportsString> htmlPrimitive(do_CreateInstance(NS_SUPPORTS_STRING_CONTRACTID));
	if (!htmlPrimitive)
		return NS_ERROR_FAILURE;
	htmlPrimitive->SetData(mHtmlString);
	trans->SetTransferData(kHTMLMime, htmlPrimitive, mHtmlString.Length() * sizeof(PRUnichar));
*/
	// add the plain (unicode) text. we use the url for text/unicode data if an anchor
	// is being dragged, rather than the title text of the link or the alt text for
	// an anchor image. 
	nsCOMPtr<nsISupportsString> textPrimitive(do_CreateInstance(NS_SUPPORTS_STRING_CONTRACTID));
	if (!textPrimitive) {
	  return NS_ERROR_FAILURE;
	}
	nsAutoString dragData(NS_LITERAL_STRING("Vpe Transferable"));
	textPrimitive->SetData(dragData);
	trans->SetTransferData(kHTMLContext, textPrimitive, dragData.Length() * sizeof(PRUnichar));

	nsCOMPtr<nsISupportsString> htmlPrimitive(do_CreateInstance(NS_SUPPORTS_STRING_CONTRACTID));
	if (!htmlPrimitive) {
		return NS_ERROR_FAILURE;
	}
	nsAutoString mHtmlString(NS_LITERAL_STRING("vpe-element"));
	htmlPrimitive->SetData(mHtmlString);
	trans->SetTransferData(kUnicodeMime, htmlPrimitive, mHtmlString.Length() * sizeof(PRUnichar));

	*outTrans = trans;
	NS_IF_ADDREF(*outTrans);
	return NS_OK;
}

nsresult
DragListener::IsDragableNode(nsIDOMNode *aNode, PRBool & IsDragable)
{
	IsDragable = PR_TRUE;
	PRBool hasAttributes;
	nsresult res = aNode->HasAttributes(&hasAttributes);
	if (NS_FAILED(res)) {
		return res;
	}

	if (hasAttributes) {
		nsCOMPtr<nsIDOMNamedNodeMap> attributes;
		nsresult res = aNode->GetAttributes(getter_AddRefs(attributes));
		if (NS_FAILED(res)) {
			return res;
		}

		PRUint32 length;
		res = attributes->GetLength( &length );
		if (NS_FAILED(res)) {
			return res;
		}

		for (PRUint32 i = 0; i < length; i++) {
			nsCOMPtr<nsIDOMNode> node;
			res = attributes->Item(i, getter_AddRefs(node));
			if (NS_FAILED(res)) {
				return res;
			}
			
			nsAutoString nodeName;
			res = node->GetNodeName(nodeName);
			if (NS_FAILED(res)) {
				return res;
			}

			if (nodeName.EqualsWithConversion(MOZ_ANONCLASS, PR_TRUE)) {
#ifdef _DEBUG
				cout << "C:DragListener::IsDragableNode| attribute is " << NS_LossyConvertUTF16toASCII(nodeName).get() << endl;
#endif
				IsDragable = PR_FALSE;
				break;
			}
		}
	}
	return NS_OK;
}

nsresult
NS_NewDragDropListener(nsIDOMEventListener ** aInstancePtrResult, nsIPresShell *aPresShell,
					   nsIDOMDocument *aDOMDocument, IVpeDragDropListener * aListener,
					   const nsAString &aPathSeparator)
{
	DragListener* it = new DragListener(aPresShell, aDOMDocument, aListener, aPathSeparator);
	if (nsnull == it) {
		return NS_ERROR_OUT_OF_MEMORY;
	}
	
	//	it->SetEditor(aEditor);
	//	it->SetPresShell(aPresShell);
	
	return it->QueryInterface(NS_GET_IID(nsIDOMEventListener), (void **) aInstancePtrResult);   
}
