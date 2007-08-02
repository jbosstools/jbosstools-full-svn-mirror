#include "nsIGenericFactory.h"

#include "nsIComponentManager.h"
#include "nsIComponentRegistrar.h"

#include "VpeResizer.h"

static const nsIID kIComponentRegistrarIID = NS_ICOMPONENTREGISTRAR_IID;

static NS_METHOD VpeResizerRegistrationProc(nsIComponentManager *aCompMgr,
											nsIFile *aPath,
											const char *registryLocation,
											const char *componentType,
											const nsModuleComponentInfo *info) {												
  nsIComponentRegistrar* compReg = nsnull;

  nsresult rv = aCompMgr->QueryInterface(kIComponentRegistrarIID, (void**)&compReg);
  if (NS_FAILED(rv))
	return rv;

  rv = compReg->RegisterFactoryLocation(kVpeResizerCID,
	"Visual Page Editor Resizer Class",
	nsnull,
	aPath,
	registryLocation,
	componentType);

  compReg->Release();
  
  return rv;
}


static NS_METHOD VpeResizerUnregistrationProc(nsIComponentManager *aCompMgr,
												nsIFile *aPath,
												const char *registryLocation,
												const nsModuleComponentInfo *info) {												
  nsIComponentRegistrar* compReg = nsnull;

  nsresult rv = aCompMgr->QueryInterface(kIComponentRegistrarIID, (void**)&compReg);
  if (NS_FAILED(rv))
	return rv;

  rv = compReg->UnregisterFactoryLocation(kVpeResizerCID, aPath);

  compReg->Release();

  return rv;
}

NS_GENERIC_FACTORY_CONSTRUCTOR(VpeResizer)

static const nsModuleComponentInfo components[] =
{
  {
  "Visual Page Editor Resizer Component",
  NS_VPERESIZER_CID,
  NS_VPERESIZER_CONTRACTID,
  VpeResizerConstructor,
  VpeResizerRegistrationProc, // registration
  VpeResizerUnregistrationProc, // unregistration
  NULL, // factory destructor
  NULL,
  NULL,
  NULL
  }
};

NS_IMPL_NSGETMODULE(VpeResizerModule, components)
