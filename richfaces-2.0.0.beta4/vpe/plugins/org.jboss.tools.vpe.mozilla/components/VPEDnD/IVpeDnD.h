/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM IVPEDnD.idl
 */

#ifndef __gen_IVpeDnD_h__
#define __gen_IVpeDnD_h__


#ifndef __gen_nsISupports_h__
#include "nsISupports.h"
#endif

/* For IDL files that don't want to include root IDL files. */
#ifndef NS_NO_VTABLE
#define NS_NO_VTABLE
#endif
#include "nsAString.h"
class nsIDOMDocument; /* forward declaration */

class nsIPresShell; /* forward declaration */

class nsIDOMNode; /* forward declaration */

class nsIDOMElement; /* forward declaration */

class IVpeDragDropListener; /* forward declaration */


/* starting interface:    IVpeDnD */
#define IVPEDND_IID_STR "a5ea4500-600f-4716-a17d-827eef6bd126"

#define IVPEDND_IID \
  {0xa5ea4500, 0x600f, 0x4716, \
    { 0xa1, 0x7d, 0x82, 0x7e, 0xef, 0x6b, 0xd1, 0x26 }}

class NS_NO_VTABLE IVpeDnD : public nsISupports {
 public: 

  NS_DEFINE_STATIC_IID_ACCESSOR(IVPEDND_IID)

  /* void Init (in nsIDOMDocument aDOMDocument, in nsIPresShell aPresShell, in IVpeDragDropListener aListener, in AString pathSeparator); */
  NS_IMETHOD Init(nsIDOMDocument *aDOMDocument, nsIPresShell *aPresShell, IVpeDragDropListener *aListener, const nsAString & pathSeparator) = 0;

  /* void GetBounds (in nsIDOMNode aNode, out PRInt32 aX, out PRInt32 aY, out PRInt32 aWidth, out PRInt32 aHeight); */
  NS_IMETHOD GetBounds(nsIDOMNode *aNode, PRInt32 *aX, PRInt32 *aY, PRInt32 *aWidth, PRInt32 *aHeight) = 0;

  /* void SetCursor (in AString aCursorName, in PRInt32 aLock); */
  NS_IMETHOD SetCursor(const nsAString & aCursorName, PRInt32 aLock) = 0;

};

/* Use this macro when declaring classes that implement this interface. */
#define NS_DECL_IVPEDND \
  NS_IMETHOD Init(nsIDOMDocument *aDOMDocument, nsIPresShell *aPresShell, IVpeDragDropListener *aListener, const nsAString & pathSeparator); \
  NS_IMETHOD GetBounds(nsIDOMNode *aNode, PRInt32 *aX, PRInt32 *aY, PRInt32 *aWidth, PRInt32 *aHeight); \
  NS_IMETHOD SetCursor(const nsAString & aCursorName, PRInt32 aLock); 

/* Use this macro to declare functions that forward the behavior of this interface to another object. */
#define NS_FORWARD_IVPEDND(_to) \
  NS_IMETHOD Init(nsIDOMDocument *aDOMDocument, nsIPresShell *aPresShell, IVpeDragDropListener *aListener, const nsAString & pathSeparator) { return _to Init(aDOMDocument, aPresShell, aListener, pathSeparator); } \
  NS_IMETHOD GetBounds(nsIDOMNode *aNode, PRInt32 *aX, PRInt32 *aY, PRInt32 *aWidth, PRInt32 *aHeight) { return _to GetBounds(aNode, aX, aY, aWidth, aHeight); } \
  NS_IMETHOD SetCursor(const nsAString & aCursorName, PRInt32 aLock) { return _to SetCursor(aCursorName, aLock); } 

/* Use this macro to declare functions that forward the behavior of this interface to another object in a safe way. */
#define NS_FORWARD_SAFE_IVPEDND(_to) \
  NS_IMETHOD Init(nsIDOMDocument *aDOMDocument, nsIPresShell *aPresShell, IVpeDragDropListener *aListener, const nsAString & pathSeparator) { return !_to ? NS_ERROR_NULL_POINTER : _to->Init(aDOMDocument, aPresShell, aListener, pathSeparator); } \
  NS_IMETHOD GetBounds(nsIDOMNode *aNode, PRInt32 *aX, PRInt32 *aY, PRInt32 *aWidth, PRInt32 *aHeight) { return !_to ? NS_ERROR_NULL_POINTER : _to->GetBounds(aNode, aX, aY, aWidth, aHeight); } \
  NS_IMETHOD SetCursor(const nsAString & aCursorName, PRInt32 aLock) { return !_to ? NS_ERROR_NULL_POINTER : _to->SetCursor(aCursorName, aLock); } 

#if 0
/* Use the code below as a template for the implementation class for this interface. */

/* Header file */
class _MYCLASS_ : public IVpeDnD
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_IVPEDND

  _MYCLASS_();
  virtual ~_MYCLASS_();
  /* additional members */
};

/* Implementation file */
NS_IMPL_ISUPPORTS1(_MYCLASS_, IVpeDnD)

_MYCLASS_::_MYCLASS_()
{
  /* member initializers and constructor code */
}

_MYCLASS_::~_MYCLASS_()
{
  /* destructor code */
}

/* void Init (in nsIDOMDocument aDOMDocument, in nsIPresShell aPresShell, in IVpeDragDropListener aListener, in AString pathSeparator); */
NS_IMETHODIMP _MYCLASS_::Init(nsIDOMDocument *aDOMDocument, nsIPresShell *aPresShell, IVpeDragDropListener *aListener, const nsAString & pathSeparator)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void GetBounds (in nsIDOMNode aNode, out PRInt32 aX, out PRInt32 aY, out PRInt32 aWidth, out PRInt32 aHeight); */
NS_IMETHODIMP _MYCLASS_::GetBounds(nsIDOMNode *aNode, PRInt32 *aX, PRInt32 *aY, PRInt32 *aWidth, PRInt32 *aHeight)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* void SetCursor (in AString aCursorName, in PRInt32 aLock); */
NS_IMETHODIMP _MYCLASS_::SetCursor(const nsAString & aCursorName, PRInt32 aLock)
{
    return NS_ERROR_NOT_IMPLEMENTED;
}

/* End of implementation class template. */
#endif


#endif /* __gen_IVpeDnD_h__ */
