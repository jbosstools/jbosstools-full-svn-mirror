// VpeResizerMouseMotionListener.cpp: implementation of the VpeResizerMouseMotionListener class.
//
//////////////////////////////////////////////////////////////////////

#include "VpeResizerMouseMotionListener.h"

#include "IVpeResizer.h"
#include "nsIDOMMouseEvent.h"

#ifdef _DEBUG
#include <iostream>
using namespace std;
#endif

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

NS_IMPL_ISUPPORTS2(VpeResizerMouseMotionListener, nsIDOMEventListener, nsIDOMMouseMotionListener)

VpeResizerMouseMotionListener::VpeResizerMouseMotionListener(VpeResizer *resizer)
:mResizer(resizer)
{

}

VpeResizerMouseMotionListener::~VpeResizerMouseMotionListener()
{

}

NS_IMETHODIMP
VpeResizerMouseMotionListener::MouseMove(nsIDOMEvent* aMouseEvent)
{
	nsCOMPtr<nsIDOMMouseEvent> mouseEvent ( do_QueryInterface(aMouseEvent) );
	if (!mouseEvent) {
		//non-ui event passed in.  bad things.
		return NS_OK;
	}
	
	// Don't do anything special if not an HTML object resizer editor
	nsCOMPtr<IVpeResizer> objectResizer = do_QueryInterface(mResizer);
	if (objectResizer)
	{
		// check if we have to redisplay a resizing shadow
		objectResizer->MouseMove(aMouseEvent);
	}
	return NS_OK;
}

NS_IMETHODIMP
VpeResizerMouseMotionListener::DragMove(nsIDOMEvent* aMouseEvent)
{
#ifdef _DEBUG
	cout << "C:VpeResizerMouseMotionListener::DragMove| " << endl;
#endif
	return NS_OK;
}

NS_IMETHODIMP
VpeResizerMouseMotionListener::HandleEvent(nsIDOMEvent* event)
{
#ifdef _DEBUG
	cout << "C:VpeResizerMouseMotionListener::HandleEvent| " << endl;
#endif
	return NS_OK;
}
