import re,sys
for name in sys.argv[1:]:
    base=f'src/view/{name}'
    f=open(base+'.form',encoding='utf-8').read()
    f2,n1=re.subn(r'\n[ \t]*<Events>.*?</Events>','',f,flags=re.S)
    open(base+'.form','w',encoding='utf-8',newline='\n').write(f2)
    j=open(base+'.java',encoding='utf-8').read()
    a=j.index('//GEN-BEGIN:initComponents'); b=j.index('//GEN-END:initComponents')
    reg=j[a:b]
    reg,m1=re.subn(r'\n[ \t]*\w+\.add\w+Listener\(new [\w.]+\(\) \{.*?\n[ \t]*\}\);',' ',reg,flags=re.S)
    reg=reg.replace(' \n','\n') if False else reg
    reg,m2=re.subn(r'\n[ \t]*\w+\.add\w+Listener\([^\n]*\);','',reg)
    j=j[:a]+reg+j[b:]
    open(base+'.java','w',encoding='utf-8',newline='\n').write(j)
    print(name,'form events',n1,'java multi',m1,'single',m2)
