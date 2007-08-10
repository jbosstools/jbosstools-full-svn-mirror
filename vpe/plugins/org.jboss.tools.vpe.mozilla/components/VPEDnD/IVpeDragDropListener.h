/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM IVpeDragDropListener.idl
 */

#ifndef __gen_IVpeDragDropListener_h__
#define __gen_IVpeDragDropListener_h__


#ifndef __gen_nsISupports_h__
#include "nsISupports.h"
#endif

/* For IDL files that don't want to include root IDL files. */
#ifndef NS_NO_VTABLE
#define NS_NO_VTABLE
#endif
#include "nsAString.h"
class nsIDOMEvent; /* forward declaration */

class nsIDOMNode; /* forward declaration */


/* starting interface:    IVpeDragDropListener */
#define IVPEDRAGDROPLISTENER_IID_STR "7e84cc02-d8e0-44fe-89cd-62a48b0cace0"

#define IVPEDRAGDROPLISTENER_IID \
  {0x7e84cc02, 0xd8e0, 0x44fe, \
    { 0x89, 0xcd, 0x62, 0xa4, 0x8b, 0x0c, 0xac, 0xe0 }}

class NS_NO_VTABLE IVpeDragDropListener : public nsISupports {
 public: 

  NS_DEFINE_STATIC_IID_ACCESSOR(IVPEDRAGDROPLISTENER_IID)

  /* void CanDrag (in nsIDOMEvent aDropEvent, out PRInt32 aResult, out nsIDOMNode aNode, out PRInt32 aOffcet); */
  NS_IMETHOD CanDrag(nsIDOMEvent *aDropEvent, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet) = 0;

  /* void CanDrop (in nsIDOMEvent aDropEvent, out PRInt32 aResult, out nsIDOMNode aNode, out PRInt32 aOffcet); */
  NS_IMETHOD CanDrop(nsIDOMEvent *aDropEvent, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet) = 0;

  /* void Drop (in nsIDOMEvent aDropEvent, out nsIDOMNode aNode, out PRInt32 aOffcet); */
  NS_IMETHOD Drop(nsIDOMEvent *aDropEvent, nsIDOMNode **aNode, PRInt32 *aOffcet) = 0;

  /* void CanDropExternal (in nsIDOMEvent aDropEvent, in AString aFlavor, in AString aTransferData, out PRInt32 aResult, out nsIDOMNode aNode, out PRInt32 aOffcet); */
  NS_IMETHOD CanDropExternal(nsIDOMEvent *aDropEvent, const nsAString & aFlavor, const nsAString & aTransferData, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet) = 0;

  /* void DropExternal (in nsIDOMEvent aDropEvent, in AString aFlavor, in AString aTransferData, out nsIDOMNode aNode, out PRInt32 aOffcet); */
  NS_IMETHOD DropExternal(nsIDOMEvent *aDropEvent, const nsAString & aFlavor, const nsAString & aTransferData, nsIDOMNode **aNode, PRInt32 *aOffcet) = 0;

};

/* Use this macro when declaring classes that implement this interface. */
#define NS_DECL_IVPEDRAGDROPLISTENER \
  NS_IMETHOD CanDrag(nsIDOMEvent *aDropEvent, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet); \
  NS_IMETHOD CanDrop(nsIDOMEvent *aDropEvent, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet); \
  NS_IMETHOD Drop(nsIDOMEvent *aDropEvent, nsIDOMNode **aNode, PRInt32 *aOffcet); \
  NS_IMETHOD CanDropExternal(nsIDOMEvent *aDropEvent, const nsAString & aFlavor, const nsAString & aTransferData, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet); \
  NS_IMETHOD DropExternal(nsIDOMEvent *aDropEvent, const nsAString & aFlavor, const nsAString & aTransferData, nsIDOMNode **aNode, PRInt32 *aOffcet); 

/* Use this macro to declare functions that forward the behavior of this interface to another object. */
#define NS_FORWARD_IVPEDRAGDROPLISTENER(_to) \
  NS_IMETHOD CanDrag(nsIDOMEvent *aDropEvent, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet) { return _to CanDrag(aDropEvent, aResult, aNode, aOffcet); } \
  NS_IMETHOD CanDrop(nsIDOMEvent *aDropEvent, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet) { return _to CanDrop(aDropEvent, aResult, aNode, aOffcet); } \
  NS_IMETHOD Drop(nsIDOMEvent *aDropEvent, nsIDOMNode **aNode, PRInt32 *aOffcet) { return _to Drop(aDropEvent, aNode, aOffcet); } \
  NS_IMETHOD CanDropExternal(nsIDOMEvent *aDropEvent, const nsAString & aFlavor, const nsAString & aTransferData, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet) { return _to CanDropExternal(aDropEvent, aFlavor, aTransferData, aResult, aNode, aOffcet); } \
  NS_IMETHOD DropExternal(nsIDOMEvent *aDropEvent, const nsAString & aFlavor, const nsAString & aTransferData, nsIDOMNode **aNode, PRInt32 *aOffcet) { return _to DropExternal(aDropEvent, aFlavor, aTransferData, aNode, aOffcet); } 

/* Use this macro to declare functions that forward the behavior of this interface to another object in a safe way. */
#define NS_FORWARD_SAFE_IVPEDRAGDROPLISTENER(_to) \
  NS_IMETHOD CanDrag(nsIDOMEvent *aDropEvent, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet) { return !_to ? NS_ERROR_NULL_POINTER : _to->CanDrag(aDropEvent, aResult, aNode, aOffcet); } \
  NS_IMETHOD CanDrop(nsIDOMEvent *aDropEvent, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet) { return !_to ? NS_ERROR_NULL_POINTER : _to->CanDrop(aDropEvent, aResult, aNode, aOffcet); } \
  NS_IMETHOD Drop(nsIDOMEvent *aDropEvent, nsIDOMNode **aNode, PRInt32 *aOffcet) { return !_to ? NS_ERROR_NULL_POINTER : _to->Drop(aDropEvent, aNode, aOffcet); } \
  NS_IMETHOD CanDropExternal(nsIDOMEvent *aDropEvent, const nsAString & aFlavor, const nsAString & aTransferData, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet) { return !_to ? NS_ERROR_NULL_POINTER : _to->CanDropExternal(aDropEvent, aFlavor, aTransferData, aResult, aNode, aOffcet); } \
  NS_IMETHOD DropExternal(nsIDOMEvent *aDropEvent, const nsAString & aFlavor, const nsAString & aTransferData, nsIDOMNode **aNode, PRInt32 *aOffcet) { return !_to ? NS_ERROR_NULL_POINTER : _to->DropExternal(aDropEvent, aFlavor, aTransferData, aNode, aOffcet); } 

#if 0
/* Use the code below as a template for the implementation class for this interface. */

/* Header file */
class _MYCLASS_ : public IVpeDragDropListener
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_IVPEDRAGDROPLISTENER

  _MYCLASS_();
  virtual ~_MYCLASS_();
  /* additional members */
};

/* Implementation file */
NS_IMPL_ISUPPORTS1(_MYCLASS_, IVpeDragDropListener)

_MYCLASS_::_MYCLASS_()
{
  /* member initializers and constructor code */
}

_MYCLASS_::~_MYCLASS_()
{
  /* destructor code */
}

/* void CanDrag (in nsIDOMEvent aDropEvent, out PRInt32 aResult, out nsIDOMNode aNode, out PRInt32 aOffcet); */
NS_IMETHODIMP _MYCLASS_::CanDrag(nsIDOMEvent *aDropEvent, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void CanDrop (in nsIDOMEvent aDropEvent, out PRInt32 aResult, out nsIDOMNode aNode, out PRInt32 aOffcet); */
NS_IMETHODIMP _MYCLASS_::CanDrop(nsIDOMEvent *aDropEvent, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void Drop (in nsIDOMEvent aDropEvent, out nsIDOMNode aNode, out PRInt32 aOffcet); */
NS_IMETHODIMP _MYCLASS_::Drop(nsIDOMEvent *aDropEvent, nsIDOMNode **aNode, PRInt32 *aOffcet)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void CanDropExternal (in nsIDOMEvent aDropEvent, in AString aFlavor, in AString aTransferData, out PRInt32 aResult, out nsIDOMNode aNode, out PRInt32 aOffcet); */
NS_IMETHODIMP _MYCLASS_::CanDropExternal(nsIDOMEvent *aDropEvent, const nsAString & aFlavor, const nsAString & aTransferData, PRInt32 *aResult, nsIDOMNode **aNode, PRInt32 *aOffcet)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void DropExternal (in nsIDOMEvent aDropEvent, in AString aFlavor, in AString aTransferData, out nsIDOMNode aNode, out PRInt32 aOffcet); */
NS_IMETHODIMP _MYCLASS_::DropExternal(nsIDOMEvent *aDropEvent, const nsAString & aFlavor, const nsAString & aTransferData, nsIDOMNode **aNode, PRInt32 *aOffcet)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* End of implementation class template. */
#endif


#endif /* __gen_IVpeDragDropListener_h__ */
