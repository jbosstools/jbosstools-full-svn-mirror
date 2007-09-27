#include "nsIGenericFactory.h"

#include "nsIComponentManager.h"
#include "nsIComponentRegistrar.h"

#include "VpeDnD.h"

static const nsIID kIComponentRegistrarIID = NS_ICOMPONENTREGISTRAR_IID;

static NS_METHOD VpeDnDRegistrationProc(nsIComponentManager *aCompMgr,
										nsIFile *aPath,
										const char *registryLocation,
										const char *componentType,
										const nsModuleComponentInfo *info) {												
  nsIComponentRegistrar* compReg = nsnull;

  nsresult rv = aCompMgr->QueryInterface(kIComponentRegistrarIID, (void**)&compReg);
  if (NS_FAILED(rv))
	return rv;

  rv = compReg->RegisterFactoryLocation(kVpeDnDCID,
	"Visual Page Editor Drag And Drop Class",
	nsnull,
	aPath,
	registryLocation,
	componentType);

  compReg->Release();
  
  return rv;
}


static NS_METHOD VpeDnDUnregistrationProc(nsIComponentManager *aCompMgr,
										  nsIFile *aPath,
										  const char *registryLocation,
										  const nsModuleComponentInfo *info) {												
  nsIComponentRegistrar* compReg = nsnull;

  nsresult rv = aCompMgr->QueryInterface(kIComponentRegistrarIID, (void**)&compReg);
  if (NS_FAILED(rv))
	return rv;

  rv = compReg->UnregisterFactoryLocation(kVpeDnDCID, aPath);

  compReg->Release();

  return rv;
}

NS_GENERIC_FACTORY_CONSTRUCTOR(VpeDnD)

static const nsModuleComponentInfo components[] =
{
  {
  "Visual Page Editor Drag And Drop Component",
  NS_VPEDND_CID,
  NS_VPEDND_CONTRACTID,
  VpeDnDConstructor,
  VpeDnDRegistrationProc, // registration
  VpeDnDUnregistrationProc, // unregistration
  NULL, // factory destructor
  NULL,
  NULL,
  NULL
  }
};

NS_IMPL_NSGETMODULE(VpeDnDModule, components)
