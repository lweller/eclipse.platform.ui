package org.eclipse.ui.tests.api;
import junit.framework.TestCase;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.PlatformUI;

public class IPerspectiveRegistryTest extends TestCase {

	private IPerspectiveRegistry fReg;
	
	public IPerspectiveRegistryTest( String testName )
	{
		super( testName );
	}
	
	public void setUp()
	{
		fReg = PlatformUI.getWorkbench().getPerspectiveRegistry();
	}
	
	public void testFindPerspectiveWithId()
	{		
		IPerspectiveDescriptor pers = ( IPerspectiveDescriptor )Tool.pick( fReg.getPerspectives() );
		
		IPerspectiveDescriptor suspect = fReg.findPerspectiveWithId( pers.getId() );
		assertNotNull( suspect );
		assertEquals( pers, suspect );
				
		suspect = fReg.findPerspectiveWithId( Tool.FakeID );
		assertNull( suspect );
	}
	
	public void testFindPerspectiveWithLabel()
	{
		IPerspectiveDescriptor pers = ( IPerspectiveDescriptor )Tool.pick( fReg.getPerspectives() );
		
		IPerspectiveDescriptor suspect = fReg.findPerspectiveWithLabel( pers.getLabel() );
		assertNotNull( suspect );
		assertEquals( pers, suspect );
				
		suspect = fReg.findPerspectiveWithLabel( Tool.FakeLabel );
		assertNull( suspect );
	}
	
	public void testGetDefaultPerspective()
	{
		String id = fReg.getDefaultPerspective();
		assertNotNull( id );
		
		IPerspectiveDescriptor suspect = fReg.findPerspectiveWithId( id );
		assertNotNull( suspect );
	}
	
	public void testSetDefaultPerspective()
	{
		IPerspectiveDescriptor pers = ( IPerspectiveDescriptor )Tool.pick( fReg.getPerspectives() );			
		fReg.setDefaultPerspective( pers.getId() );
		
		assertEquals( pers.getId(), fReg.getDefaultPerspective() );
	}
	
	public void testGetPerspectives() throws Throwable
	{	
		IPerspectiveDescriptor[] pers = fReg.getPerspectives();
		assertNotNull( pers );
		
		for( int i = 0; i < pers.length; i ++ )
			assertNotNull( pers[ i ] );
	}
}