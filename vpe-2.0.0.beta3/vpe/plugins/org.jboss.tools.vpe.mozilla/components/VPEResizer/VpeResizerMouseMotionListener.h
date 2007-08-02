// VpeResizerMouseMotionListener.h: interface for the VpeResizerMouseMotionListener class.
//
//////////////////////////////////////////////////////////////////////

#if !defined(AFX_VPERESIZERMOUSEMOTIONLISTENER_H__7AF84ED5_EE88_4BB7_A34C_EF089C373DA2__INCLUDED_)
#define AFX_VPERESIZERMOUSEMOTIONLISTENER_H__7AF84ED5_EE88_4BB7_A34C_EF089C373DA2__INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "nsWeakReference.h"
#include "nsIDOMMouseMotionListener.h"
#include "VpeResizer.h"

class VpeResizerMouseMotionListener : public nsIDOMMouseMotionListener  
{
public:
	VpeResizerMouseMotionListener(VpeResizer *resizer);
	virtual ~VpeResizerMouseMotionListener();

	NS_DECL_ISUPPORTS
		
	NS_DECL_NSIDOMEVENTLISTENER
		
	NS_IMETHOD MouseMove(nsIDOMEvent* aMouseEvent);
	NS_IMETHOD DragMove(nsIDOMEvent* aMouseEvent);
private:
	VpeResizer *mResizer;
};

#endif // !defined(AFX_VPERESIZERMOUSEMOTIONLISTENER_H__7AF84ED5_EE88_4BB7_A34C_EF089C373DA2__INCLUDED_)
