/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM IVpeResizer.idl
 */

#ifndef __gen_IVpeResizer_h__
#define __gen_IVpeResizer_h__


#ifndef __gen_nsISupports_h__
#include "nsISupports.h"
#endif

/* For IDL files that don't want to include root IDL files. */
#ifndef NS_NO_VTABLE
#define NS_NO_VTABLE
#endif
class nsIDOMDocument; /* forward declaration */

class nsIPresShell; /* forward declaration */

class nsIDOMElement; /* forward declaration */

class nsIDOMEvent; /* forward declaration */

class IVpeResizeListener; /* forward declaration */

class nsIDOMWindow; /* forward declaration */


/* starting interface:    IVpeResizer */
#define IVPERESIZER_IID_STR "c5673ccf-eb08-4cf4-b189-a674c6d3b4a2"

#define IVPERESIZER_IID \
  {0xc5673ccf, 0xeb08, 0x4cf4, \
    { 0xb1, 0x89, 0xa6, 0x74, 0xc6, 0xd3, 0xb4, 0xa2 }}

class NS_NO_VTABLE IVpeResizer : public nsISupports {
 public: 

  NS_DEFINE_STATIC_IID_ACCESSOR(IVPERESIZER_IID)

  enum { eTopLeft = 1 };

  enum { eTop = 2 };

  enum { eTopRight = 4 };

  enum { eLeft = 8 };

  enum { eRight = 16 };

  enum { eBottomLeft = 32 };

  enum { eBottom = 64 };

  enum { eBottomRight = 128 };

  /* void Init (in nsIDOMDocument aDOMDocument, in nsIPresShell aPresShell); */
  NS_IMETHOD Init(nsIDOMDocument *aDOMDocument, nsIPresShell *aPresShell) = 0;

  /* void Show (in nsIDOMElement aElement, in PRInt32 resizers); */
  NS_IMETHOD Show(nsIDOMElement *aElement, PRInt32 resizers) = 0;

  /* void Hide (); */
  NS_IMETHOD Hide(void) = 0;

  /* void AddResizeListener (in IVpeResizeListener aListener); */
  NS_IMETHOD AddResizeListener(IVpeResizeListener *aListener) = 0;

  /* void RemoveResizeListener (in IVpeResizeListener aListener); */
  NS_IMETHOD RemoveResizeListener(IVpeResizeListener *aListener) = 0;

  /* void MouseDown (in PRInt32 aX, in PRInt32 aY, in nsIDOMElement target); */
  NS_IMETHOD MouseDown(PRInt32 aX, PRInt32 aY, nsIDOMElement *target) = 0;

  /* void MouseMove (in nsIDOMEvent event); */
  NS_IMETHOD MouseMove(nsIDOMEvent *event) = 0;

  /* void MouseUp (in PRInt32 aX, in PRInt32 aY, in nsIDOMElement target); */
  NS_IMETHOD MouseUp(PRInt32 aX, PRInt32 aY, nsIDOMElement *target) = 0;

};

/* Use this macro when declaring classes that implement this interface. */
#define NS_DECL_IVPERESIZER \
  NS_IMETHOD Init(nsIDOMDocument *aDOMDocument, nsIPresShell *aPresShell); \
  NS_IMETHOD Show(nsIDOMElement *aElement, PRInt32 resizers); \
  NS_IMETHOD Hide(void); \
  NS_IMETHOD AddResizeListener(IVpeResizeListener *aListener); \
  NS_IMETHOD RemoveResizeListener(IVpeResizeListener *aListener); \
  NS_IMETHOD MouseDown(PRInt32 aX, PRInt32 aY, nsIDOMElement *target); \
  NS_IMETHOD MouseMove(nsIDOMEvent *event); \
  NS_IMETHOD MouseUp(PRInt32 aX, PRInt32 aY, nsIDOMElement *target); 

/* Use this macro to declare functions that forward the behavior of this interface to another object. */
#define NS_FORWARD_IVPERESIZER(_to) \
  NS_IMETHOD Init(nsIDOMDocument *aDOMDocument, nsIPresShell *aPresShell) { return _to Init(aDOMDocument, aPresShell); } \
  NS_IMETHOD Show(nsIDOMElement *aElement, PRInt32 resizers) { return _to Show(aElement, resizers); } \
  NS_IMETHOD Hide(void) { return _to Hide(); } \
  NS_IMETHOD AddResizeListener(IVpeResizeListener *aListener) { return _to AddResizeListener(aListener); } \
  NS_IMETHOD RemoveResizeListener(IVpeResizeListener *aListener) { return _to RemoveResizeListener(aListener); } \
  NS_IMETHOD MouseDown(PRInt32 aX, PRInt32 aY, nsIDOMElement *target) { return _to MouseDown(aX, aY, target); } \
  NS_IMETHOD MouseMove(nsIDOMEvent *event) { return _to MouseMove(event); } \
  NS_IMETHOD MouseUp(PRInt32 aX, PRInt32 aY, nsIDOMElement *target) { return _to MouseUp(aX, aY, target); } 

/* Use this macro to declare functions that forward the behavior of this interface to another object in a safe way. */
#define NS_FORWARD_SAFE_IVPERESIZER(_to) \
  NS_IMETHOD Init(nsIDOMDocument *aDOMDocument, nsIPresShell *aPresShell) { return !_to ? NS_ERROR_NULL_POINTER : _to->Init(aDOMDocument, aPresShell); } \
  NS_IMETHOD Show(nsIDOMElement *aElement, PRInt32 resizers) { return !_to ? NS_ERROR_NULL_POINTER : _to->Show(aElement, resizers); } \
  NS_IMETHOD Hide(void) { return !_to ? NS_ERROR_NULL_POINTER : _to->Hide(); } \
  NS_IMETHOD AddResizeListener(IVpeResizeListener *aListener) { return !_to ? NS_ERROR_NULL_POINTER : _to->AddResizeListener(aListener); } \
  NS_IMETHOD RemoveResizeListener(IVpeResizeListener *aListener) { return !_to ? NS_ERROR_NULL_POINTER : _to->RemoveResizeListener(aListener); } \
  NS_IMETHOD MouseDown(PRInt32 aX, PRInt32 aY, nsIDOMElement *target) { return !_to ? NS_ERROR_NULL_POINTER : _to->MouseDown(aX, aY, target); } \
  NS_IMETHOD MouseMove(nsIDOMEvent *event) { return !_to ? NS_ERROR_NULL_POINTER : _to->MouseMove(event); } \
  NS_IMETHOD MouseUp(PRInt32 aX, PRInt32 aY, nsIDOMElement *target) { return !_to ? NS_ERROR_NULL_POINTER : _to->MouseUp(aX, aY, target); } 

#if 0
/* Use the code below as a template for the implementation class for this interface. */

/* Header file */
class _MYCLASS_ : public IVpeResizer
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_IVPERESIZER

  _MYCLASS_();
  virtual ~_MYCLASS_();
  /* additional members */
};

/* Implementation file */
NS_IMPL_ISUPPORTS1(_MYCLASS_, IVpeResizer)

_MYCLASS_::_MYCLASS_()
{
  /* member initializers and constructor code */
}

_MYCLASS_::~_MYCLASS_()
{
  /* destructor code */
}

/* void Init (in nsIDOMDocument aDOMDocument, in nsIPresShell aPresShell); */
NS_IMETHODIMP _MYCLASS_::Init(nsIDOMDocument *aDOMDocument, nsIPresShell *aPresShell)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void Show (in nsIDOMElement aElement, in PRInt32 resizers); */
NS_IMETHODIMP _MYCLASS_::Show(nsIDOMElement *aElement, PRInt32 resizers)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void Hide (); */
NS_IMETHODIMP _MYCLASS_::Hide()
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void AddResizeListener (in IVpeResizeListener aListener); */
NS_IMETHODIMP _MYCLASS_::AddResizeListener(IVpeResizeListener *aListener)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void RemoveResizeListener (in IVpeResizeListener aListener); */
NS_IMETHODIMP _MYCLASS_::RemoveResizeListener(IVpeResizeListener *aListener)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void MouseDown (in PRInt32 aX, in PRInt32 aY, in nsIDOMElement target); */
NS_IMETHODIMP _MYCLASS_::MouseDown(PRInt32 aX, PRInt32 aY, nsIDOMElement *target)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void MouseMove (in nsIDOMEvent event); */
NS_IMETHODIMP _MYCLASS_::MouseMove(nsIDOMEvent *event)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void MouseUp (in PRInt32 aX, in PRInt32 aY, in nsIDOMElement target); */
NS_IMETHODIMP _MYCLASS_::MouseUp(PRInt32 aX, PRInt32 aY, nsIDOMElement *target)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* End of implementation class template. */
#endif


#endif /* __gen_IVpeResizer_h__ */
