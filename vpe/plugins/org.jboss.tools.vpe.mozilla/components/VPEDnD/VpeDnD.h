#ifndef _VPEDND__H_
#define _VPEDND__H_
#include "IVpeDnD.h"

#include "nsWeakReference.h"
class nsIDOMEventListener;
class nsIDOMEventReceiver;
class nsIDOMElement;

#define NS_VPEDND_CID_STR	"8DCA89BF-2660-49a7-AC4B-7834474A0A52"

#define NS_VPEDND_CID	\
{ 0x8DCA89BF, 0x2660, 0x49a7, \
{ 0xAC, 0x4B, 0x78, 0x34, 0x47, 0x4A, 0x0A, 0x52 } }

#define NS_VPEDND_CONTRACTID	"@jboss.org/editorext/dnd;1"

static const nsCID kVpeDnDCID = NS_VPEDND_CID;

class VpeDnD : public IVpeDnD
{
public:
	NS_DECL_ISUPPORTS
	NS_DECL_IVPEDND
		
	VpeDnD();
	virtual ~VpeDnD();
	/* additional members */
private:
	nsWeakPtr	m_DocWeak;
	nsWeakPtr	m_PresShellWeak;

	nsCOMPtr<nsIDOMEventListener>	m_DragDropListener;
	nsCOMPtr<nsIDOMElement>			m_BodyElement;

	nsresult GetDOMEventReceiver(nsIDOMEventReceiver **aEventReceiver);
	nsresult GetRootElement(nsIDOMElement **aBodyElement);
};

#endif
