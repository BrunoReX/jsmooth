#include <windows.h>
#include "JavasoftRuntimeList.h"

#include <iostream>
#include <vector>
#include <string>

#include "resource.h"
#include "ResourceManager.h"

void SaveJarResource();

/*  Declare Windows procedure  */
LRESULT CALLBACK WindowProcedure (HWND, UINT, WPARAM, LPARAM);

/*  Make the class name into a global variable  */
char szClassName[ ] = "WindowsApp";

JavasoftRuntimeList *jlist;

std::vector< std::string > LOG;


void addResId(std::string& msg, LPCTSTR id)
{
   if ((ULONG)id & 0xFFFF0000) 
    {
        msg += id;
    }
    else
    {
        char buffer[200];
        sprintf(buffer, "%u", (int)((long)id & 0xFFFF));
        msg += buffer;
    }
}

BOOL CALLBACK lpEnumFunc(HMODULE hModule,   // module handle
                        LPCTSTR lpszType, // pointer to resource type
                        LPTSTR lpszName,  // pointer to resource name
                        LONG lParam       // application-defined parameter
                        )
{
    std::string msg = "Found NAME: ";
    addResId(msg, lpszName);
    msg += " for ";
    addResId(msg, lpszType);
    
    LOG.push_back(msg);
    return TRUE;
}


BOOL CALLBACK  rsctypecallback(HMODULE module, LPTSTR lpszType, LONG dummy)
{
    std::string msg = "Found TYPE: ";
    addResId(msg, lpszType);
    LOG.push_back(msg);
    
     EnumResourceNames(module, lpszType, lpEnumFunc, dummy);
    
    return TRUE;
}
                          
int WINAPI WinMain (HINSTANCE hThisInstance,
                    HINSTANCE hPrevInstance,
                    LPSTR lpszArgument,
                    int nFunsterStil)

{
    HWND hwnd;               /* This is the handle for our window */
    MSG messages;            /* Here messages to the application are saved */
    WNDCLASSEX wincl;        /* Data structure for the windowclass */

    jlist = new JavasoftRuntimeList();
//    jlist->run();
    
    /* The Window structure */
    wincl.hInstance = hThisInstance;
    wincl.lpszClassName = szClassName;
    wincl.lpfnWndProc = WindowProcedure;      /* This function is called by windows */
    wincl.style = CS_DBLCLKS | CS_HREDRAW | CS_VREDRAW;                 /* Catch double-clicks */
    wincl.cbSize = sizeof (WNDCLASSEX);

    /* Use default icon and mouse-pointer */
    wincl.hIcon = LoadIcon (NULL, IDI_APPLICATION);
    wincl.hIconSm = LoadIcon (NULL, IDI_APPLICATION);
    wincl.hCursor = LoadCursor (NULL, IDC_ARROW);
    wincl.lpszMenuName = NULL;                 /* No menu */
    wincl.cbClsExtra = 0;                      /* No extra bytes after the window class */
    wincl.cbWndExtra = 0;                      /* structure or the window instance */
    /* Use Windows's default color as the background of the window */
    wincl.hbrBackground = (HBRUSH) COLOR_BACKGROUND;

    /* Register the window class, and if it fails quit the program */
    if (!RegisterClassEx (&wincl))
        return 0;

    /* The class is registered, let's create the program*/

    hwnd = CreateWindowEx (
           0,                   /* Extended possibilites for variation */
           szClassName,         /* Classname */
           "Windows App",       /* Title Text */
           WS_OVERLAPPEDWINDOW, /* default window */
           CW_USEDEFAULT,       /* Windows decides the position */
           CW_USEDEFAULT,       /* where the window ends up on the screen */
           544,                 /* The programs width */
           375,                 /* and height in pixels */
           HWND_DESKTOP,        /* The window is a child-window to desktop */
           NULL,                /* No menu */
           hThisInstance,       /* Program Instance handler */
           NULL                 /* No Window Creation data */
           );

    /* Make the window visible on the screen */
    ShowWindow (hwnd, nFunsterStil);
    UpdateWindow(hwnd);

     HRSRC res = FindResource(NULL, "#153", "MIN2");
    if (res != NULL)
    {
        LOG.push_back(std::string("OK !!! well done ! "));
    }
    else
    {
            LOG.push_back("Can't find Resource");

    }

    EnumResourceNames(NULL, "MINE", lpEnumFunc, 0);

    EnumResourceTypes(NULL, rsctypecallback, 0);


    ResourceManager manager("JAVA", 102, 103);
    LOG.push_back(manager.getMainName());
    manager.saveTemp(std::string("POUET"));
    
    SaveJarResource();

    /* Run the message loop. It will run until GetMessage() returns 0 */
    while (GetMessage (&messages, NULL, 0, 0))
    {
        /* Translate virtual-key messages into character messages */
        TranslateMessage(&messages);
        /* Send message to WindowProcedure */
        DispatchMessage(&messages);
    }

    /* The program return-value is 0 - The value that PostQuitMessage() gave */
    return messages.wParam;
}


/*  This function is called by the Windows function DispatchMessage()  */

LRESULT CALLBACK WindowProcedure (HWND hwnd, UINT message, WPARAM wParam, LPARAM lParam)
{
    HDC hdc;
	PAINTSTRUCT ps;

    char buffer[255];    
    char * mesg = "NOT!  !  ! !! ! ! !! ! ";
    mesg = buffer;

//   LOG.push_back("Test");

//    HRSRC res = FindResource(NULL, "#113", "MATYPE");
//    if (res != NULL)
//    {
//        sprintf(buffer,"OK %d      ! ! !! !! ", res);
//    }
//    else
//    {
//            sprintf(buffer,"NOK %d      ! ! !! !! ", res);
//    }

//    int res2 = LoadString(NULL, 214, buffer, 200);
//    if (res2 >0)
//        mesg = "WELL DONE !!!!! ! ! !! ";
    
    switch (message)                  /* handle the messages */
    {
		case WM_PAINT:
			hdc = BeginPaint(hwnd, &ps);
			// TODO: Add any drawing code here...
			RECT rt;
			GetClientRect(hwnd, &rt);
			DrawText(hdc, mesg, 10, &rt, DT_CENTER);
			
			for (int i=0; i<jlist->getCount(); i++)
			{
			   JavasoftVM vm = jlist->getAt(i);
			   std::string str = vm.Version + ": " + vm.Path;

			   std::string::size_type length = str.length();
			   char *p = new char [length + 1];
			   str.copy(p, length);
			   p[length] = '\0';
			   rt.top += 20;
			   DrawText(hdc, p, length, &rt, DT_CENTER);
			   delete p;
			}
			{
                  char buffer[255];
                  jlist->copyString(jlist->Message, buffer, 254);
                  rt.top += 40;
                  DrawText(hdc, buffer, jlist->Message.size(), &rt, DT_CENTER);
                  
            }	
            
            for (int i=0; i<LOG.size(); i++)
            {
                       std::string val = LOG[i];
                        rt.top += 20;
                     DrawText(hdc, val.c_str(), strlen(val.c_str()), &rt, DT_CENTER);
            }
              		
			EndPaint(hwnd, &ps);
			break;

        case WM_DESTROY:
            PostQuitMessage (0);       /* send a WM_QUIT to the message queue */
            break;
        default:                      /* for messages that we don't deal with */
            return DefWindowProc (hwnd, message, wParam, lParam);
    }

    return 0;
}

void SaveJarResource()
{
    HRSRC resmain = FindResource(NULL, "#102", "JAVA");
    if (resmain != NULL)
    {
        LOG.push_back("Found resource resmain!");
    }
}
