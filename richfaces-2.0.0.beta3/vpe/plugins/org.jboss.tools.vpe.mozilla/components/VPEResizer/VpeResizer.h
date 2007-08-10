/* Header file */
#ifndef _VpeResizer__h_
#define _VpeResizer__h_

#define NS_VPERESIZER_CID_STR	"E11C1DBE-3CF1-4228-B022-DD0D5AD20766"

#define NS_VPERESIZER_CID	\
{ 0xe11c1dbe, 0x3cf1, 0x4228, \
{ 0xb0, 0x22, 0xdd, 0xd, 0x5a, 0xd2, 0x7, 0x66 } }

#define NS_VPERESIZER_CONTRACTID	"@jboss.org/editorext/resizer;1"

#include "IVpeResizer.h"

#include "nsCOMArray.h"
#include "nsString.h"
#include "nsWeakReference.h"
#include "nsIDOMNode.h"
#include "nsIContent.h"
#include "nsIDOMEventReceiver.h"
#include "IVpeResizeListener.h"

static const nsCID kVpeResizerCID = NS_VPERESIZER_CID;

#define kTopLeft       NS_LITERAL_STRING("nw")
#define kTop           NS_LITERAL_STRING("n")
#define kTopRight      NS_LITERAL_STRING("ne")
#define kLeft          NS_LITERAL_STRING("w")
#define kRight         NS_LITERAL_STRING("e")
#define kBottomLeft    NS_LITERAL_STRING("sw")
#define kBottom        NS_LITERAL_STRING("s")
#define kBottomRight   NS_LITERAL_STRING("se")

class VpeResizer : public IVpeResizer
{
public:
  NS_DECL_ISUPPORTS
  NS_DECL_IVPERESIZER

  VpeResizer();
  virtual ~VpeResizer();
  /* additional members */
private:
  enum {
    kX,
    kY,
    kWidth,
    kHeight
  };
	
  nsWeakPtr m_DocWeak;
  nsWeakPtr m_PresShellWeak;
  nsCOMPtr<nsIDOMElement> m_ResizingObject;
  nsCOMPtr<nsIDOMElement> m_ResizingShadow;
  nsCOMPtr<nsIDOMElement> m_ResizingInfo;
  nsCOMPtr<nsIDOMElement> m_BodyElement;

  nsCOMPtr<nsIDOMEventListener> m_MouseListener;
  nsCOMPtr<nsIDOMEventListener> m_MouseMotionListener;
  nsCOMPtr<nsIDOMEventListener> m_DragEnterListener;
  

  nsCOMArray<IVpeResizeListener> objectResizeEventListeners;
  
  short m_usedHandle;

  nsCOMPtr<nsIDOMElement> m_ActiveHandle;
  nsCOMPtr<nsIDOMElement> m_TopLeftHandle;
  nsCOMPtr<nsIDOMElement> m_TopHandle;
  nsCOMPtr<nsIDOMElement> m_TopRightHandle;
  nsCOMPtr<nsIDOMElement> m_LeftHandle;
  nsCOMPtr<nsIDOMElement> m_RightHandle;
  nsCOMPtr<nsIDOMElement> m_BottomLeftHandle;
  nsCOMPtr<nsIDOMElement> m_BottomHandle;
  nsCOMPtr<nsIDOMElement> m_BottomRightHandle;
  

  PRBool m_Resizing;

  PRInt32 m_OriginalX;
  PRInt32 m_OriginalY;

  PRInt32 m_ResizedObjectX;
  PRInt32 m_ResizedObjectY;
  PRInt32 m_ResizedObjectWidth;
  PRInt32 m_ResizedObjectHeight;
  
  PRInt32 m_ResizedObjectMarginLeft;
  PRInt32 m_ResizedObjectMarginTop;
  PRInt32 m_ResizedObjectBorderLeft;
  PRInt32 m_ResizedObjectBorderTop;

  PRInt32 m_XIncrementFactor;
  PRInt32 m_YIncrementFactor;
  PRInt32 m_WidthIncrementFactor;
  PRInt32 m_HeightIncrementFactor;
  
  PRInt8  m_InfoXIncrement;
  PRInt8  m_InfoYIncrement;

  nsresult GetRootElement(nsIDOMElement **aBodyElement);
  nsresult CreateResizer(nsIDOMElement ** aReturn, const nsAString &aLocation, nsIDOMNode * aParentNode);
  nsresult CreateAnonymousElement(const nsAString & aTag, nsIDOMNode *  aParentNode,
	  const nsAString & aAnonClass, PRBool aIsCreatedHidden, nsIDOMElement ** aReturn);
  nsresult CreateHTMLContent(const nsAString& aTag, nsIContent** aContent);
  nsresult GetPositionAndDimensions(nsIDOMElement * aElement, PRInt32 & aX, PRInt32 & aY,
	  PRInt32 & aW, PRInt32 & aH, PRInt32 & aBorderLeft, PRInt32 & aBorderTop,
	  PRInt32 & aMarginLeft, PRInt32 & aMarginTop);
  nsresult GetElementOrigin(nsIDOMElement * aElement, PRInt32 & aX, PRInt32 & aY);
  nsresult SetAllResizersPosition();
  nsresult SetStyle(nsIDOMElement *aElement,const nsAString &aPropertyName,const nsAString &aValue);
  nsresult SetStylePropertyPixels(nsIDOMElement *aElement, const nsAString & aProperty, PRInt32 aValue);
  nsresult StartResizing(nsIDOMElement *aTarget);
  nsresult GetDOMEventReceiver(nsIDOMEventReceiver **aEventReceiver);
  void HandleEventListenerError();
  nsresult CreateShadow(nsIDOMElement ** aReturn, nsIDOMNode * aParentNode,
	  nsIDOMElement * aOriginalObject);
  nsresult SetShadowPosition(nsIDOMElement * aShadow, nsIDOMElement * aOriginalObject,
	  PRInt32 aOriginalObjectX, PRInt32 aOriginalObjectY);
  nsresult SetAnonymousElementPosition(PRInt32 aX, PRInt32 aY,nsIDOMElement *aElement);
  PRInt32 GetNewResizingX(PRInt32 aX, PRInt32 aY);
  PRInt32 GetNewResizingY(PRInt32 aX, PRInt32 aY);
  PRInt32 GetNewResizingWidth(PRInt32 aX, PRInt32 aY);
  PRInt32 GetNewResizingHeight(PRInt32 aX, PRInt32 aY);
  PRInt32 GetNewResizingIncrement(PRInt32 aX, PRInt32 aY, PRInt32 aID);
  void SetResizeIncrements(PRInt32 aX, PRInt32 aY,
	  PRInt32 aW, PRInt32 aH, PRBool aPreserveRatio);
  void HideShadowAndInfo();
  void EndResizing(PRInt32 aClientX, PRInt32 aClientY);
  nsresult CreateResizingInfo(nsIDOMElement ** aReturn, nsIDOMNode * aParentNode);
  void SetInfoIncrements(PRInt8 aX, PRInt8 aY);
  nsresult SetResizingInfoPosition(PRInt32 aX, PRInt32 aY, PRInt32 aW, PRInt32 aH);
};

#endif
