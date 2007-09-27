/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM IVpeDropListener.idl
 */

#ifndef __gen_IVpeDropListener_h__
#define __gen_IVpeDropListener_h__


#ifndef __gen_nsISupports_h__
#include "nsISupports.h"
#endif

/* For IDL files that don't want to include root IDL files. */
#ifndef NS_NO_VTABLE
#define NS_NO_VTABLE
#endif
class nsIDOMElement; /* forward declaration */


/* starting interface:    IVpeDropListener */
#define IVPEDROPLISTENER_IID_STR "7e84cc02-d8e0-44fe-89cd-62a48b0cace0"

#define IVPEDROPLISTENER_IID \
  {0x7e84cc02, 0xd8e0, 0x44fe, \
    { 0x89, 0xcd, 0x62, 0xa4, 0x8b, 0x0c, 0xac, 0xe0 }}

class NS_NO_VTABLE IVpeDropListener : public nsISupports {
 public: 

  NS_DEFINE_STATIC_IID_ACCESSOR(IVPEDROPLISTENER_IID)

  /* void CanDrop (in PRInt32 aClientX, in PRInt32 aClientY, in nsIDOMElement aElement, in nsIDOMElement aDropOnElement, out PRInt32 aResult); */
  NS_IMETHOD CanDrop(PRInt32 aClientX, PRInt32 aClientY, nsIDOMElement *aElement, nsIDOMElement *aDropOnElement, PRInt32 *aResult) = 0;

  /* void Drop (in PRInt32 aClientX, in PRInt32 aClientY, in nsIDOMElement aElement); */
  NS_IMETHOD Drop(PRInt32 aClientX, PRInt32 aClientY, nsIDOMElement *aElement) = 0;

};

/* Use this macro when declaring classes that implement this interface. */
#define NS_DECL_IVPEDROPLISTENER \
  NS_IMETHOD CanDrop(PRInt32 aClientX, PRInt32 aClientY, nsIDOMElement *aElement, nsIDOMElement *aDropOnElement, PRInt32 *aResult); \
  NS_IMETHOD Drop(PRInt32 aClientX, PRInt32 aClientY, nsIDOMElement *aElement); 

/* Use this macro to declare functions that forward the behavior of this interface to another object. */
#define NS_FORWARD_IVPEDROPLISTENER(_to) \
  NS_IMETHOD CanDrop(PRInt32 aClientX, PRInt32 aClientY, nsIDOMElement *aElement, nsIDOMElement *aDropOnElement, PRInt32 *aResult) { return _to CanDrop(aClientX, aClientY, aElement, aDropOnElement, aResult); } \
  NS_IMETHOD Drop(PRInt32 aClientX, PRInt32 aClientY, nsIDOMElement *aElement) { return _to Drop(aClientX, aClientY, aElement); } 

/* Use this macro to declare functions that forward the behavior of this interface to another object in a safe way. */
#define NS_FORWARD_SAFE_IVPEDROPLISTENER(_to) \
  NS_IMETHOD CanDrop(PRInt32 aClientX, PRInt32 aClientY, nsIDOMElement *aElement, nsIDOMElement *aDropOnElement, PRInt32 *aResult) { return !_to ? NS_ERROR_NULL_POINTER : _to->CanDrop(aClientX, aClientY, aElement, aDropOnElement, aResult); } \
  NS_IMETHOD Drop(PRInt32 aClientX, PRInt32 aClientY, nsIDOMElement *aElement) { return !_to ? NS_ERROR_NULL_POINTER : _to->Drop(aClientX, aClientY, aElement); } 

#if 0
/* Use the code below as a template for the implementation class for this interface. */

/* Header file */
class _MYCLASS_ : public IVpeDropListener
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_IVPEDROPLISTENER

  _MYCLASS_();
  virtual ~_MYCLASS_();
  /* additional members */
};

/* Implementation file */
NS_IMPL_ISUPPORTS1(_MYCLASS_, IVpeDropListener)

_MYCLASS_::_MYCLASS_()
{
  /* member initializers and constructor code */
}

_MYCLASS_::~_MYCLASS_()
{
  /* destructor code */
}

/* void CanDrop (in PRInt32 aClientX, in PRInt32 aClientY, in nsIDOMElement aElement, in nsIDOMElement aDropOnElement, out PRInt32 aResult); */
NS_IMETHODIMP _MYCLASS_::CanDrop(PRInt32 aClientX, PRInt32 aClientY, nsIDOMElement *aElement, nsIDOMElement *aDropOnElement, PRInt32 *aResult)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void Drop (in PRInt32 aClientX, in PRInt32 aClientY, in nsIDOMElement aElement); */
NS_IMETHODIMP _MYCLASS_::Drop(PRInt32 aClientX, PRInt32 aClientY, nsIDOMElement *aElement)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* End of implementation class template. */
#endif


#endif /* __gen_IVpeDropListener_h__ */
