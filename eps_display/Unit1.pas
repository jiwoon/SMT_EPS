unit Unit1;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms, Dialogs, StdCtrls, ExtCtrls, DB, ADODB, Grids, DBGrids, DBCtrls, Mask, DateUtils, ShellAPI, DBTables, StrUtils, Math;

type
  TForm1 = class(TForm)
    dataGrid: TDBGrid;
    selectQry: TADOQuery;
    mainQry: TADOQuery;
    operatorQry: TADOQuery;
    Panel1: TPanel;
    Label6: TLabel;
    Label2: TLabel;
    Label3: TLabel;
    Label4: TLabel;
    Label5: TLabel;
    Label7: TLabel;
    Label8: TLabel;
    Label1: TLabel;
    scanLineseatLb: TLabel;
    lineseatLb: TLabel;
    materialNoLb: TLabel;
    scanMaterialNoLb: TLabel;
    operatorLb: TLabel;
    typeLb: TLabel;
    resultLb: TLabel;
    lineCb: TComboBox;
    workOrderCb: TComboBox;
    boardTypeCb: TComboBox;
    ds: TDataSource;
    refreshTimer: TTimer;
    DBConnection: TADOConnection;
    procedure dataGridDrawColumnCell(Sender: TObject; const Rect: TRect; DataCol: Integer; Column: TColumn; State: TGridDrawState);
    procedure lineCbChange(Sender: TObject);
    procedure workOrderCbChange(Sender: TObject);
    procedure boardTypeCbChange(Sender: TObject);
    procedure FormCreate(Sender: TObject);
    procedure refreshTimerTimer(Sender: TObject);
    procedure updateTop();
  private

    { Private declarations }
    procedure OnMouseWheel(var Msg: TMsg; var Handled: Boolean);
  public
    { Public declarations }
  end;

var
  Form1: TForm1;

var
  num: Real;
  board_type: Real;

implementation

{$R *.dfm}

//根据数据集显示●或×
procedure TForm1.dataGridDrawColumnCell(Sender: TObject; const Rect: TRect; DataCol: Integer; Column: TColumn; State: TGridDrawState);
begin
  if (Column.FieldName = 'store_issue_result') then
  begin
    TDBGrid(Sender).Canvas.FillRect(Rect);

    if (TDBGrid(Sender).Fields[1].Value) then
    begin
      dataGrid.Canvas.Font.Color := clGreen;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '●');

    end
    else
    begin
      dataGrid.Canvas.Font.Color := clRed;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '×');
    end
  end
  else if (Column.FieldName = 'feed_result') then
  begin
    TDBGrid(Sender).Canvas.FillRect(Rect);
    if (TDBGrid(Sender).Fields[2].Value) then
    begin
      dataGrid.Canvas.Font.Color := clGreen;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, ' ●');
    end
    else
    begin
      dataGrid.Canvas.Font.Color := clRed;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '×');

    end
  end
  else if (Column.FieldName = 'change_result') then
  begin
    TDBGrid(Sender).Canvas.FillRect(Rect);
    if (TDBGrid(Sender).Fields[3].Value) then
    begin
      dataGrid.Canvas.Font.Color := clGreen;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '●');
    end
    else
    begin
      dataGrid.Canvas.Font.Color := clRed;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '×');
    end
  end
  else if (Column.FieldName = 'check_result') then
  begin
    TDBGrid(Sender).Canvas.FillRect(Rect);
    if (TDBGrid(Sender).Fields[4].Value) then
    begin
      dataGrid.Canvas.Font.Color := clGreen;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '●');
    end
    else
    begin
      dataGrid.Canvas.Font.Color := clRed;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '×');
    end
  end
  else if (Column.FieldName = 'check_all_result') then
  begin
    TDBGrid(Sender).Canvas.FillRect(Rect);
    if (TDBGrid(Sender).Fields[5].Value) then
    begin
      dataGrid.Canvas.Font.Color := clGreen;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '●');
    end
    else
    begin
      dataGrid.Canvas.Font.Color := clRed;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '×');
    end
  end
  else if (Column.FieldName = 'first_check_all_result') then
  begin
    TDBGrid(Sender).Canvas.FillRect(Rect);

    if (TDBGrid(Sender).Fields[6].Value) then
    begin
      dataGrid.Canvas.Font.Color := clGreen;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '●');
    end
    else
    begin
      dataGrid.Canvas.Font.Color := clRed;
      TDBGrid(Sender).Canvas.TextRect(Rect, Rect.Left + 2, Rect.Top + 2, '×');
    end
  end
  else
  begin

  end;
end;

//刷新数据
procedure TForm1.updateTop();
var
  strsql: string;
  typename: string;
  temp: string;
  board_type: string;
begin
  mainQry.SQL.Clear;
  operatorQry.SQL.Clear;
  //解析板面类型
  if (boardTypeCb.Text = '默认') then
  begin
    board_type := '0';
  end
  else if (boardTypeCb.Text = 'AB面') then
  begin
    board_type := '1';
  end
  else if (boardTypeCb.Text = 'A面') then
  begin
    board_type := '2';
  end
  else if (boardTypeCb.Text = 'B面') then
  begin
    board_type := '3';
  end;
  //查询Visit表
  strsql := 'SELECT program_item_visit.* FROM program_item_visit INNER JOIN program ON program.`id`=program_item_visit.`program_id` WHERE line=''' + lineCb.Text + ''' AND work_order=''' + workOrderCb.Text + ''' AND board_type=''' + board_type + ''' ORDER BY last_operation_time DESC';
  mainQry.SQL.Add(strsql);
  mainQry.Active := True;
  mainQry.Open;
  mainQry.First;
  //填充顶部界面数据
  scanLineseatLb.Caption := mainQry.fieldbyname('scan_lineseat').AsString;
  lineseatLb.Caption := mainQry.fieldbyname('lineseat').AsString;
  materialNoLb.Caption := mainQry.fieldbyname('material_no').AsString;
  scanMaterialNoLb.Caption := mainQry.fieldbyname('scan_material_no').AsString;
  typename := mainQry.fieldbyname('last_operation_type').AsString;
  //填充右上角结果
  if (typename = '0') then
  begin
    typeLb.Caption := '上料';
    temp := temp + StrUtils.IfThen(mainQry.fieldbyname('feed_result').AsString = '0', 'FAIL', 'PASS');
  end
  else if (typename = '1') then
  begin
    typeLb.Caption := '换料';
    temp := temp + StrUtils.IfThen(mainQry.fieldbyname('change_result').AsString = '0', 'FAIL', 'PASS');
  end
  else if (typename = '2') then
  begin
    typeLb.Caption := '核料';
    temp := temp + StrUtils.IfThen(mainQry.fieldbyname('check_result').AsString = '0', 'FAIL', 'PASS');
  end
  else if (typename = '3') then
  begin
    typeLb.Caption := '全检';
    temp := temp + StrUtils.IfThen(mainQry.fieldbyname('check_all_result').AsString = '0', 'FAIL', 'PASS');
  end;
  if (typename = '4') then
  begin
    typeLb.Caption := '发料';
    temp := temp + StrUtils.IfThen(mainQry.fieldbyname('store_issue_result').AsString = '0', 'FAIL', 'PASS');
  end
  else if (typename = '5') then
  begin
    typeLb.Caption := '首检';
    temp := temp + StrUtils.IfThen(mainQry.fieldbyname('first_check_all_result').AsString = '0', 'FAIL', 'PASS');
  end;
  if StrUtils.AnsiContainsText(temp, 'PASS') then
  begin
    typeLb.Color := clGreen;
    resultLb.Color := clGreen;
  end
  else
  begin
    typeLb.Color := clRed;
    resultLb.Color := clRed;
  end;
  resultLb.Caption := temp;
  mainQry.Active := True;
  //填写操作员
  operatorQry.SQL.Add('SELECT operation.operator as operator');
  operatorQry.SQL.Add('FROM program_item_visit INNER JOIN operation ON');
  operatorQry.SQL.Add('(operation.material_no=program_item_visit.material_no AND program_item_visit.`program_id`=operation.`program_id` AND program_item_visit.`lineseat`=operation.`lineseat`)');
  operatorQry.SQL.Add('WHERE line=''' + lineCb.Text + ''' AND work_order=''' + workOrderCb.Text + ''' AND board_type=''' + board_type + '''');
  operatorQry.SQL.Add('ORDER BY last_operation_time DESC LIMIT 1');
  operatorQry.Open;
  operatorLb.Caption := operatorQry.fieldbyname('operator').AsString;
end;

//选择线号后，查询出相应工单
procedure TForm1.lineCbChange(Sender: TObject);
var
  strsql: string;
begin
  workOrderCb.items.Clear;
  selectQry.SQL.Clear;

  strsql := 'select distinct work_order from program where line=''' + lineCb.Text + '''and work_order<>'''' and state=1';
  selectQry.sql.add(strsql);
  selectQry.Active := True;
  while not selectQry.eof do
  begin
    workOrderCb.items.add(selectQry.fieldByName('work_order').asstring);
    selectQry.next;
  end;

end;

//选择工单后，查询出相应板面类型
procedure TForm1.workOrderCbChange(Sender: TObject);
var
  strsql: string;
  board_type: string;
begin
  boardTypeCb.items.Clear;
  selectQry.SQL.Clear;
  strsql := ' select distinct board_type from program where work_order=''' + workOrderCb.Text + '''';
  selectQry.sql.add(strsql);
  selectQry.Active := True;
  while not selectQry.eof do
  begin
    board_type := selectQry.fieldByName('board_type').AsString;
    if (board_type = '0') then
    begin
      boardTypeCb.Items.Add('默认');
    end
    else if (board_type = '1') then
    begin
      boardTypeCb.Items.Add('AB面');
    end
    else if (board_type = '2') then
    begin
      boardTypeCb.Items.Add('A面');
    end
    else if (board_type = '3') then
    begin
      boardTypeCb.Items.Add('B面');
    end;
    selectQry.next;

  end;

end;

//选择板面类型后，更新数据，并显示表格
procedure TForm1.boardTypeCbChange(Sender: TObject);
begin
  updateTop;
  dataGrid.Show;
end;

//程序入口
procedure TForm1.FormCreate(Sender: TObject);
begin
  operatorQry.Active := False;
  dataGrid.Hide;
  Application.OnMessage := OnMouseWheel;
end;

//令表格可滚动
procedure TForm1.OnMouseWheel(var Msg: TMsg; var Handled: Boolean);
begin
  try
    if (Msg.message = WM_MouseWheel) and ((Screen.ActiveForm.ActiveControl.ClassName = 'TDBGrid') or (Screen.ActiveForm.ActiveControl.ClassName = 'TDBGridInplaceEdit')) then
    begin
      if Msg.wParam > 0 then
        SendMessage(Screen.ActiveForm.ActiveControl.Handle, WM_VSCROLL, SB_PAGEUP, 0)
      else
        SendMessage(Screen.ActiveForm.ActiveControl.Handle, WM_VSCROLL, SB_PAGEDOWN, 0);
      Handled := True;
    end;
  except
  end;
end;

//刷新数据定时器事件
procedure TForm1.refreshTimerTimer(Sender: TObject);
begin
  if (boardTypeCb.Text <> '') then
  begin
    dataGrid.DataSource.DataSet.Active := False;
    dataGrid.DataSource.DataSet.Active := True;
    updateTop;
  end;
end;

end.

