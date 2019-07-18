package usefulMethods;

import java.lang.reflect.InvocationTargetException;

public class UsefulAIMethods {

	
	/**
	 * calls method
	 * @param subject: the thing the method will be called from
	 * @param methodname: the name of the method
	 * @param args the arguments
	 */
	public static void CallMethod(Object subject, String methodname, Object... args)
	{
		try {
			if(args != null)
			{
				subject.getClass().getMethod(methodname, args.getClass()).invoke(subject, args);
			}
			else
			{
				subject.getClass().getMethod(methodname, null).invoke(subject, null);
			}
		} catch ( IllegalArgumentException |
				 SecurityException | 
				 NoSuchMethodException | 
				 IllegalAccessException | 
				 InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
