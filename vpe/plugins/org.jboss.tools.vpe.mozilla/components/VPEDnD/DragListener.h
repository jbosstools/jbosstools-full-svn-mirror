// VpeDragListener.h: interface for the VpeDragListener class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_VPEDRAGLISTENER_H__68DC8071_AAA7_4ADF_B698_09863196BC16__INCLUDED_)
#define AFX_VPEDRAGLISTENER_H__68DC8071_AAA7_4ADF_B698_09863196BC16__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "nsIDOMDragListener.h"

#include "nsCOMPtr.h"
#include "nsString.h"

class nsIPresShell;
class IVpeDragDropListener;
class nsICaret;
class nsITransferable;
class nsIBoxObject;
class nsIScrollableView;

class DragListener : public nsIDOMDragListener  
{
public:
	DragListener(nsIPresShell * aPresShell, nsIDOMDocument * aDOMDocument, IVpeDragDropListener * aListener, const nsAString &aPathSeparator);
	virtual ~DragListener();

	NS_DECL_ISUPPORTS

	/**
	* This method is called whenever an event occurs of the type for which 
	* the EventListener interface was registered.
	*
	* @param   evt The Event contains contextual information about the 
	*              event. It also contains the stopPropagation and 
	*              preventDefault methods which are used in determining the 
	*              event's flow and default action.
	*/
	/* void handleEvent (in nsIDOMEvent event); */
	NS_IMETHOD HandleEvent(nsIDOMEvent *event);

	/**
	* Processes a drag enter event
	* @param aMouseEvent @see nsIDOMEvent.h 
	* @returns whether the event was consumed or ignored. @see nsresult
	*/
	NS_IMETHOD DragEnter(nsIDOMEvent* aMouseEvent);

	/**
	* Processes a drag over event
	* @param aMouseEvent @see nsIDOMEvent.h 
	* @returns whether the event was consumed or ignored. @see nsresult
	*/
	NS_IMETHOD DragOver(nsIDOMEvent* aMouseEvent);

	/**
	* Processes a drag Exit event
	* @param aMouseEvent @see nsIDOMEvent.h 
	* @returns whether the event was consumed or ignored. @see nsresult
	*/
	NS_IMETHOD DragExit(nsIDOMEvent* aMouseEvent);

	/**
	* Processes a drag drop event
	* @param aMouseEvent @see nsIDOMEvent.h 
	* @returns whether the event was consumed or ignored. @see nsresult
	*/
	NS_IMETHOD DragDrop(nsIDOMEvent* aMouseEvent);

	/**
	* Processes a drag gesture event
	* @param aMouseEvent @see nsIDOMEvent.h 
	* @returns whether the event was consumed or ignored. @see nsresult
	*/
	NS_IMETHOD DragGesture(nsIDOMEvent* aMouseEvent);
private:
	nsCOMPtr<IVpeDragDropListener>		m_VpeDragDropListener;
	nsIPresShell				*m_PresShell;
	nsIScrollableView			*m_scrollView;
	nsString				m_PathSeparator;
	nsCOMPtr<nsIBoxObject>			m_DocEltBox;
	float					m_p2t;
	PRInt32					m_clientX;
	PRInt32					m_clientY;

//	nsIDOMDocument				*m_DOMDocument;
	nsCOMPtr<nsICaret>			m_Caret;
	PRBool					m_CaretDrawn;

#ifdef _DEBUG
	PRInt32					m_counter;
#endif


	nsresult PrepareTransferable(nsITransferable **aTransferable, PRInt32 aDnDType);
	nsresult GetDnDType(PRInt32 &aDnDType);
	nsresult GetFlavor(nsAString &aFlavor, PRInt32 aDnDType);
	nsresult GetData(nsAString &aFlavor, nsAString &aData, PRInt32 aDnDType);
	nsresult GetRangeParentAndOffset(nsIDOMEvent* aMouseEvent, nsIDOMNode** aRangeParent, PRInt32* aRangeOffset);
	nsresult CreateTransferable(nsITransferable** outTrans);
	nsresult IsDragableNode(nsIDOMNode *aNode, PRBool & IsDragable);
};

nsresult
NS_NewDragDropListener(nsIDOMEventListener ** aInstancePtrResult, nsIPresShell *aPresShell,
					   nsIDOMDocument *aDOMDocument, IVpeDragDropListener *aListener,
					   const nsAString &aPathSeparator);

#endif // !defined(AFX_VPEDRAGLISTENER_H__68DC8071_AAA7_4ADF_B698_09863196BC16__INCLUDED_)
