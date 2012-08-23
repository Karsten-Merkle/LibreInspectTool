This is a small training project about getting involved into LibreOffice forked from OpenOffice forked from StarOffice.
You should have installed the [MRI-Extension](http://extensions.libreoffice.org/extension-center/mri-uno-object-inspection-tool).

Take a look into 'unix.properties' and fit it to your system settings.
Make sure all your Office Applications are closed, and run 'ant deploy'.
Now you should have three additional context menu entries (right mouse click) to call [MRI](http://extensions.libreoffice.org/extension-center/mri-uno-object-inspection-tool).

Here are several things you may like to fiddle around:

1. Using it as it is provided here, gives you a new ProtcolHandler.
You might change the comment in
'de.karsten_merkle.officeextension.inspect.IntrospectMenuInterceptor'
from line 41 to 46 to use the MRI ProtcolHandler.

2. To Register our ProtcolHandler with TestProtocolHandler.xcu instead with JobExecutor comment in:
line 5 in 'oxtMETA-INF/manifest.xml'
line 16 to 18 and line 26 in 'de.karsten_merkle.officeextension.inspect.Registrar'
comment out:
line 61 in 'de.karsten_merkle.officeextension.inspect.JobExecutor'

3. Figure out which URL's come into your TestProtocolHandler by enabling finest in your logging.properties.
Do this using TestProtocolHandler initialised by only JobExecutor and only by TestProtocolHandler.xcu


Have fun,
Karsten Merkle