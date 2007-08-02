/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM IVpeResizeListener.idl
 */

#ifndef __gen_IVpeResizeListener_h__
#define __gen_IVpeResizeListener_h__


#ifndef __gen_nsISupports_h__
#include "nsISupports.h"
#endif

/* For IDL files that don't want to include root IDL files. */
#ifndef NS_NO_VTABLE
#define NS_NO_VTABLE
#endif
class nsIDOMElement; /* forward declaration */


/* starting interface:    IVpeResizeListener */
#define IVPERESIZELISTENER_IID_STR "29c1345e-90a1-4dd0-b525-7f27f259d9a8"

#define IVPERESIZELISTENER_IID \
  {0x29c1345e, 0x90a1, 0x4dd0, \
    { 0xb5, 0x25, 0x7f, 0x27, 0xf2, 0x59, 0xd9, 0xa8 }}

class NS_NO_VTABLE IVpeResizeListener : public nsISupports {
 public: 

  NS_DEFINE_STATIC_IID_ACCESSOR(IVPERESIZELISTENER_IID)

  /* void OnEndResizing (in PRInt32 aUsedHandle, in PRInt32 aTop, in PRInt32 aLeft, in PRInt32 aX, in PRInt32 aY, in nsIDOMElement resizedObject); */
  NS_IMETHOD OnEndResizing(PRInt32 aUsedHandle, PRInt32 aTop, PRInt32 aLeft, PRInt32 aX, PRInt32 aY, nsIDOMElement *resizedObject) = 0;

};

/* Use this macro when declaring classes that implement this interface. */
#define NS_DECL_IVPERESIZELISTENER \
  NS_IMETHOD OnEndResizing(PRInt32 aUsedHandle, PRInt32 aTop, PRInt32 aLeft, PRInt32 aX, PRInt32 aY, nsIDOMElement *resizedObject); 

/* Use this macro to declare functions that forward the behavior of this interface to another object. */
#define NS_FORWARD_IVPERESIZELISTENER(_to) \
  NS_IMETHOD OnEndResizing(PRInt32 aUsedHandle, PRInt32 aTop, PRInt32 aLeft, PRInt32 aX, PRInt32 aY, nsIDOMElement *resizedObject) { return _to OnEndResizing(aUsedHandle, aTop, aLeft, aX, aY, resizedObject); } 

/* Use this macro to declare functions that forward the behavior of this interface to another object in a safe way. */
#define NS_FORWARD_SAFE_IVPERESIZELISTENER(_to) \
  NS_IMETHOD OnEndResizing(PRInt32 aUsedHandle, PRInt32 aTop, PRInt32 aLeft, PRInt32 aX, PRInt32 aY, nsIDOMElement *resizedObject) { return !_to ? NS_ERROR_NULL_POINTER : _to->OnEndResizing(aUsedHandle, aTop, aLeft, aX, aY, resizedObject); } 

#if 0
/* Use the code below as a template for the implementation class for this interface. */

/* Header file */
class _MYCLASS_ : public IVpeResizeListener
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_IVPERESIZELISTENER

  _MYCLASS_();
  virtual ~_MYCLASS_();
  /* additional members */
};

/* Implementation file */
NS_IMPL_ISUPPORTS1(_MYCLASS_, IVpeResizeListener)

_MYCLASS_::_MYCLASS_()
{
  /* member initializers and constructor code */
}

_MYCLASS_::~_MYCLASS_()
{
  /* destructor code */
}

/* void OnEndResizing (in PRInt32 aUsedHandle, in PRInt32 aTop, in PRInt32 aLeft, in PRInt32 aX, in PRInt32 aY, in nsIDOMElement resizedObject); */
NS_IMETHODIMP _MYCLASS_::OnEndResizing(PRInt32 aUsedHandle, PRInt32 aTop, PRInt32 aLeft, PRInt32 aX, PRInt32 aY, nsIDOMElement *resizedObject)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* End of implementation class template. */
#endif


#endif /* __gen_IVpeResizeListener_h__ */
